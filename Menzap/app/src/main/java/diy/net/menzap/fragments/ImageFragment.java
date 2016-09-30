package diy.net.menzap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

import diy.net.menzap.R;
import diy.net.menzap.activity.ViewImage;
import diy.net.menzap.adapter.GridViewAdapter;


public class ImageFragment extends Fragment {

    private SwipeRefreshLayout swipeLayout;
    private GridViewAdapter gridAdapter;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    GridView grid;



    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                ImageFragment.this.refreshView();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.refreshView();
    }

    private void refreshView() {
        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getContext(), "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Menzap/Images");
            // Create a new folder if no folder named SDImageTutorial exist
            file.mkdirs();
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();

            if (listFile == null) {
                return;
            }

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }

        grid = (GridView) getView().findViewById(R.id.gridView);

        gridAdapter = new GridViewAdapter(this.getActivity(), FilePathStrings, FileNameStrings);
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeLayout.setRefreshing(false);

        grid.setAdapter(gridAdapter);


        // Capture gridview item click
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getActivity(), ViewImage.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                i.putExtra("filename", FileNameStrings);
                // Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }

        });
    }


}
