package com.customer;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Максим on 08.08.2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ContactViewHolder> {

    private LayoutInflater inflater;
    protected ArrayList<String> names = new ArrayList<>();
    protected ArrayList<String> descriptions = new ArrayList<>();
    protected ArrayList<Integer> ids = new ArrayList<>();
    Context context;

    public ListAdapter(Context context, ArrayList<String> names, ArrayList<String> descriptions, ArrayList<Integer> ids){
        inflater = LayoutInflater.from(context);
        this.names = new ArrayList<>(names);
        this.descriptions = new ArrayList<>(descriptions);
        this.ids = new ArrayList<>(ids);
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.fragment_list, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.name.setText(names.get(position));
        holder.description.setText(descriptions.get(position));
        holder.databaseID.setText(String.valueOf(ids.get(position)));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private ImageView icon;
        private TextView name;
        private TextView description;
        private TextView databaseID;
        private InputMethodManager imm;
        private ImageView delete;

        public ContactViewHolder(final View itemView, Context context) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.firsname);
            description = (TextView) itemView.findViewById(R.id.desc);
            databaseID = (TextView) itemView.findViewById(R.id.databaseID);
delete=(ImageView) itemView.findViewById(R.id.delete);

            imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);

            itemView.setOnClickListener(this);
            name.setOnClickListener(this);
            description.setOnClickListener(this);
            delete.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.delete){
                RemoveConfirmation dialog = new RemoveConfirmation();
                Bundle data = new Bundle();
                data.putString("purpose", "contacts");
                data.putInt("itemID", Integer.parseInt(databaseID.getText().toString()));
                data.putInt("position", getPosition());
                dialog.setArguments(data);
                FragmentManager fragmentManager = ((MainActivity)context).getFragmentManager();
                dialog.show(fragmentManager, "Confirmation");


            }else{
//                Intent intent = new Intent(context , MeetRecActivity.class);
//                intent.putExtra("contactID", Integer.parseInt(databaseID.getText().toString()));
//                context.startActivity(intent);
            }
        }



    }
}