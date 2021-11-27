package com.duy.compile;

import android.content.Context;
import android.os.AsyncTask;

import com.duy.compile.external.CompileHelper;
import com.duy.project.file.android.AndroidProjectFolder;

import java.io.File;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;

public class BuildApkTask extends AsyncTask<AndroidProjectFolder, Object, File> {
    private static final String TAG = "BuildApkTask";
    private Context context;
    private BuildApkTask.CompileListener listener;
    private DiagnosticCollector mDiagnosticCollector;
    private Exception error;

    public BuildApkTask(Context context, BuildApkTask.CompileListener listener) {
        this.context = context;
        this.listener = listener;
        mDiagnosticCollector = new DiagnosticCollector();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart();
    }

    @Override
    protected File doInBackground(AndroidProjectFolder... params) {
        AndroidProjectFolder projectFile = params[0];
        if (params[0] == null) return null;

        //clean
        projectFile.clean();
        try {
            return CompileHelper.buildApk(context, projectFile, mDiagnosticCollector);
        } catch (Exception e) {
            this.error = e;
        }
        return null;
    }


    @Override
    protected void onPostExecute(final File result) {
        super.onPostExecute(result);
        if (result == null) {
            listener.onError(error, mDiagnosticCollector.getDiagnostics());
        } else {
            listener.onComplete(result, mDiagnosticCollector.getDiagnostics());
        }
    }

    public interface CompileListener {
        void onStart();

        void onError(Exception e, List<Diagnostic> diagnostics);

        void onComplete(File apk, List<Diagnostic> diagnostics);

    }
}
