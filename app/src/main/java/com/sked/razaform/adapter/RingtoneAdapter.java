
package com.sked.razaform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sked.razaform.R;
import com.sked.razaform.model.Ringtone;

import java.util.List;


/**
 * Created by manish on 11/2/2015.
 */
public class RingtoneAdapter extends ArrayAdapter<Ringtone> {
    private Context context;
    private int resource;
    private List<Ringtone> ringtones;

    public RingtoneAdapter(Context context, int resource, List<Ringtone> ringtones) {
        super(context, resource, ringtones);
        this.context = context;
        this.resource = resource;
        this.ringtones = ringtones;
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(resource, null);
        TextView ringtoneName = (TextView) itemView.findViewById(R.id.ringtoneName);
        Ringtone ringtone = ringtones.get(position);
        ringtoneName.setText(ringtone.getRingtoneName());
        return itemView;
    }


}
