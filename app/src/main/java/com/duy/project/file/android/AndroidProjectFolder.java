package com.duy.project.file.android;

import android.content.Context;

import com.android.annotations.Nullable;
import com.android.ide.common.xml.AndroidManifestParser;
import com.android.ide.common.xml.ManifestData;
import com.duy.ide.file.FileManager;
import com.duy.project.file.java.ClassFile;
import com.duy.project.file.java.JavaProjectFolder;
import com.google.common.base.MoreObjects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Duy on 05-Aug-17.
 */
public class AndroidProjectFolder extends JavaProjectFolder {
    private File xmlManifest;
    private File resourceFile;
    /* Output */
    private File apkUnsigned;
    private File apkUnaligned;
    private KeyStore keystore;
    /* PROJECT */
    private File dirRes;
    private File dirAssets;
    private File classR;
    private File dirOutApk;
    private ManifestData.Activity launcherActivity;
    private ArrayList<String> modules = new ArrayList<>();

    public AndroidProjectFolder(File dirRoot,
                                @Nullable String mainClassName,
                                @Nullable String packageName,
                                String projectName) {
        super(dirRoot, mainClassName, packageName, projectName);
    }

    @Override
    public void init() {
        super.init();
        dirRes = new File(dirSrcMain, "res");
        dirAssets = new File(dirSrcMain, "assets");
        xmlManifest = new File(dirSrcMain, "AndroidManifest.xml");

        dirOutApk = new File(dirOutput, "apk");
        apkUnsigned = new File(dirOutput, "app-unsigned-debug.apk");
        apkUnaligned = new File(dirOutput, "app-unaligned-debug.apk");

        createClassR();

        resourceFile = new File(dirBuild, "resources.ap_");
        dexedClassesFile = new File(dirBuild, "classes.dex");
        keystore = new KeyStore(new File(dirProject, "keystore.jks"),
                "android".toCharArray(),
                "android",
                "android".toCharArray());
    }

    @Nullable
    public ManifestData.Activity getLauncherActivity() {
        try {
            ManifestData manifestData = AndroidManifestParser.parse(new FileInputStream(getXmlManifest()));
            ManifestData.Activity launcherActivity = manifestData.getLauncherActivity();
            this.launcherActivity = launcherActivity;
            return launcherActivity;
        } catch (Exception e) {
            return null;
        }
    }


    private void createClassR() {
        if (packageName != null) {
            String path = packageName.replace(".", File.separator) + File.separator + "R.java";
            classR = new File(dirGeneratedSource, path);
        }
    }

    public void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }

    public KeyStore getKeyStore() {
        return keystore;
    }

    public File getXmlManifest() {
        return xmlManifest;
    }

    public File getApkUnaligned() throws IOException {
        return apkUnaligned;
    }

    public File getResourceFile() throws IOException {
        if (!resourceFile.exists()) {
            resourceFile.getParentFile().mkdirs();
            resourceFile.createNewFile();
        }
        return resourceFile;
    }

    @Override
    public void clean() {
        super.clean();
        apkUnsigned.delete();
        apkUnaligned.delete();
    }

    @Override
    public void mkdirs() {
        super.mkdirs();
        getDirRes();
        getDirAssets();

        File menu = new File(dirRes, "menu");
        if (!menu.exists()) menu.mkdirs();
        File layout = new File(dirRes, "layout");
        if (!layout.exists()) layout.mkdirs();
        File drawable = new File(dirRes, "drawable");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-hdpi");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-xhdpi");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-xxhdpi");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-xxxhdpi");
        if (!drawable.exists()) drawable.mkdirs();
        File value = new File(dirRes, "values");
        if (!value.exists()) value.mkdirs();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

    @Override
    public String getSourcePath() {
        String sourcePath = super.getSourcePath();
        File generatedSource = new File(dirGenerated, "source");
        sourcePath += File.pathSeparator + generatedSource.getPath();
        return sourcePath;
    }

    public File getApkUnsigned() throws IOException {
        if (!apkUnsigned.exists()) {
            apkUnsigned.getParentFile().mkdirs();
            apkUnsigned.createNewFile();
            apkUnsigned.setReadable(true);
        }
        return apkUnsigned;
    }

    public File getDirRes() {
        if (!dirRes.exists()) {
            dirRes.mkdirs();
            dirRes.setReadable(true);
        }
        return dirRes;
    }

    public File getDirAssets() {
        if (!dirAssets.exists()) {
            dirAssets.mkdirs();
            dirAssets.setReadable(true);
        }
        return dirAssets;
    }

    public File getClassR() throws IOException {
        if (classR == null) createClassR();
        if (!classR.exists()) {
            classR.getParentFile().mkdirs();
            classR.createNewFile();
            classR.setReadable(true);
        }
        return classR;
    }

    public File getDirLayout() {
        File file = new File(getDirRes(), "layout");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * use for javac
     *
     * @return main class
     */
    @Override
    public ClassFile getMainClass() {
        if (launcherActivity == null) getLauncherActivity();
        if (launcherActivity != null) {
            return new ClassFile(this.launcherActivity.getName());
        } else return null;
    }

    public void checkKeyStoreExits(Context context) {
        if (!keystore.getFile().exists()) {
            File key = new File(dirProject, "keystore.jks");
            if (!key.getParentFile().exists()) {
                key.getParentFile().mkdirs();
            }
            try {
                key.createNewFile();
                FileOutputStream out = new FileOutputStream(key);
                FileManager.copyStream(context.getAssets().open(Constants.KEY_STORE_ASSET_PATH), out);
                out.close();
                setKeystore(new KeyStore(key, Constants.KEY_STORE_PASSWORD,
                        Constants.KEY_STORE_ALIAS, Constants.KEY_STORE_ALIAS_PASS));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
