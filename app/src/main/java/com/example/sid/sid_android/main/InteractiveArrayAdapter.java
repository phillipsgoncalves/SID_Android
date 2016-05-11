package com.example.sid.sid_android.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sid.sid_android.R;
import com.example.sid.sid_android.database.DatabaseHandler;
import com.example.sid.sid_android.util.Advertisement;
import com.example.sid.sid_android.util.Company;

import java.util.List;

public class InteractiveArrayAdapter extends ArrayAdapter<Advertisement> {

    private final List<Advertisement> list;
    private final Activity context;
    private final DatabaseHandler handler;

    public InteractiveArrayAdapter(Activity context, List<Advertisement> list, DatabaseHandler handler) {
        super(context, R.layout.rowbuttonlayout, list);
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    static class ViewHolder {
        protected TextView text;
        protected ImageView image;
        protected Button button;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.rowbuttonlayout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.button = (Button) view.findViewById(R.id.second);

            if (list.get(position).getEstado().equals("Y")) {
                viewHolder.image.setImageResource(R.drawable.yes);
            } else if (list.get(position).getEstado().equals("N")) {
                viewHolder.image.setImageResource(R.drawable.no);
            } else {
                viewHolder.image.setImageResource(R.drawable.unknown);
            }
            viewHolder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Informação Empresa");
                    Company c = handler.getCompany(list.get(position).getEmail());
                    alertDialogBuilder.setMessage(c.toString());
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }

                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            viewHolder.image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Bolsa Traducoes");
                    alertDialogBuilder.setMessage("Tem a certeza que pretende aceitar o seguinte trabalho?");
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Aceitar",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    viewHolder.image.setImageResource(R.drawable.yes);
                                    list.get(position).setEstado("Y");
                                    handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                                }

                            });
                    alertDialogBuilder.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            viewHolder.image.setImageResource(R.drawable.no);
                            list.get(position).setEstado("N");
                            handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                        }

                    });
                    alertDialogBuilder.setNeutralButton("Pendente", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            viewHolder.image.setImageResource(R.drawable.unknown);
                            list.get(position).setEstado("I");
                            handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                        }

                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            });
            view.setTag(viewHolder);
            viewHolder.image.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).image.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).toString());
        return view;
    }

}

