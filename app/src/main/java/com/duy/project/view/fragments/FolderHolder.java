package com.duy.project.view.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duy.ide.R;
import com.duy.ide.themefont.fonts.FontManager;
import com.duy.project.utils.ProjectFileUtil;
import com.unnamed.b.atv.model.TreeNode;

import java.io.File;

import static com.duy.project.ProjectFileContract.Callback;
import static com.duy.project.ProjectFileContract.FileActionListener;


public class FolderHolder extends TreeNode.BaseNodeViewHolder<FolderHolder.TreeItem> {
    private static final String TAG = "FolderHolder";
    private TextView txtName;
    private LayoutInflater inflater;
    private ImageView imgArrow;
    private boolean leaf = false;

    public FolderHolder(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final TreeItem item) {
        View view = inflater.inflate(R.layout.list_item_file, null, false);
        txtName = view.findViewById(R.id.node_value);
        txtName.setTypeface(FontManager.getFontFromAsset(context, "Roboto-Light.ttf"));
        txtName.setText(item.getFile().getName());

        imgArrow = view.findViewById(R.id.img_arrow);
        this.leaf = node.isLeaf();
        View imgNew = view.findViewById(R.id.img_add);
        View imgDelete = view.findViewById(R.id.img_delete);

        if (item.getFile().isDirectory() && !node.isRoot()) {
            imgNew.setVisibility(View.VISIBLE);
        } else {
            imgNew.setVisibility(View.GONE);
        }
        if (ProjectFileUtil.isRoot(item.getProjectFile(), item.getFile())) {
            imgDelete.setVisibility(View.GONE);
        } else {
            imgDelete.setVisibility(View.VISIBLE);
        }
        if (node.isLeaf()) {
            imgArrow.setVisibility(View.INVISIBLE);
        }

        final File file = item.getFile();
        setIcon((ImageView) view.findViewById(R.id.img_icon), file);

        final FileActionListener listener = item.getListener();
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.clickRemoveFile(file, new Callback() {
                        @Override
                        public void onSuccess(File old) {
                            getTreeView().removeNode(node);
                        }

                        @Override
                        public void onFailed(@Nullable Exception e) {

                        }
                    });
                }
            }
        });
        imgNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.clickCreateNewFile(file, new Callback() {
                        @Override
                        public void onSuccess(File newf) {
                            TreeNode child = new TreeNode(new TreeItem(item.getProjectFile(), newf, listener));
                            getTreeView().addNode(node, child);
                        }

                        @Override
                        public void onFailed(@Nullable Exception e) {

                        }
                    });
                }
            }
        });

        return view;
    }


    private void setIcon(ImageView view, File fileDetail) {
        String fileName = fileDetail.getName();
        if (fileDetail.isDirectory()) {
            view.setImageResource(R.drawable.ic_folder_green);
        } else if (fileName.endsWith(".java")) {
            view.setImageResource(R.drawable.ic_java_file_yellow);
        } else if (fileName.endsWith(".jar")) {
            view.setImageResource(R.drawable.ic_jar_file_white);
        } else if (fileName.endsWith(".class")) {
            view.setImageResource(R.drawable.ic_class_file_white);
        } else if (fileName.endsWith(".xml")) {
            view.setImageResource(R.drawable.ic_xml_file_white);
        } else {
            view.setImageResource(R.drawable.ic_insert_drive_file_white_24dp);
        }
    }

    @Override
    public void toggle(boolean active) {
        if (!leaf) {
            imgArrow.setImageResource(active ? R.drawable.ic_keyboard_arrow_down_white_24dp :
                    R.drawable.ic_keyboard_arrow_right_white_24dp);
        }
    }


    public static class TreeItem {
        private File projectFile;
        @NonNull
        private File file;
        @Nullable
        private FileActionListener listener;

        public TreeItem(@NonNull File projectFile, @Nullable File file,
                        @Nullable FileActionListener listener) {
            this.projectFile = projectFile;
            this.file = file;
            this.listener = listener;
        }

        public File getProjectFile() {
            return projectFile;
        }

        @Nullable
        public FileActionListener getListener() {
            return listener;
        }

        @NonNull
        public File getFile() {
            return file;
        }
    }
}
