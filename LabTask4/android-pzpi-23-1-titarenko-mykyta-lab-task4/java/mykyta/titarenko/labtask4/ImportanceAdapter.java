package mykyta.titarenko.labtask4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ImportanceAdapter extends ArrayAdapter<ImportanceItem> {

    public ImportanceAdapter(@NonNull Context context, List<ImportanceItem> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImportanceItem item = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);

        ImageView imageView = convertView.findViewById(R.id.importanceIcon);
        TextView textView = convertView.findViewById(R.id.importanceTextView);

        if (item.GetImageResId() != null){
            imageView.setImageResource(item.GetImageResId());
        }

        textView.setText(item.GetTitle());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position, convertView, parent);
    }
}
