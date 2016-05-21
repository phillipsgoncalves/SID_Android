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
    private String email;
    private String password;

    public InteractiveArrayAdapter(Activity context, List<Advertisement> list, DatabaseHandler handler, String email, String password) {
        super(context, R.layout.rowbuttonlayout, list);
        this.context = context;
        this.list = list;
        this.handler = handler;
        this.email = email;
        this.password = password;
    }

    static class ViewHolder {
        protected ImageView image;
        protected Button button;
        protected ImageView icon;
        protected TextView ling_origem;
        protected TextView ling_destino;
        protected TextView num_palavras;
        protected TextView valor;
        protected TextView data;
        protected TextView dias;
        protected TextView software;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        /** bug parece resolvido, escrever no relatorio **/

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.rowbuttonlayout, null);
            viewHolder = new ViewHolder();

            viewHolder.ling_origem = (TextView) convertView.findViewById(R.id.ling_origem);
            viewHolder.ling_destino = (TextView) convertView.findViewById(R.id.ling_destino);
            viewHolder.num_palavras = (TextView) convertView.findViewById(R.id.num_palavras);
            viewHolder.valor = (TextView) convertView.findViewById(R.id.valor);
            viewHolder.data = (TextView) convertView.findViewById(R.id.data);
            viewHolder.dias = (TextView) convertView.findViewById(R.id.dias);
            viewHolder.software = (TextView) convertView.findViewById(R.id.software);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.imageView);

            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.button = (Button) convertView.findViewById(R.id.second);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Informação Empresa");

                    System.out.println(list.get(position).toString());



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

            final ViewHolder finalViewHolder = viewHolder;
            finalViewHolder.image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Bolsa Traducoes");
                    alertDialogBuilder.setMessage("Tem a certeza que pretende aceitar o seguinte trabalho?");
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Aceitar",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    finalViewHolder.image.setImageResource(R.drawable.yes);
                                    list.get(position).setEstado("Y");
                                    handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                                    handler.updateRelacaoTrad(list.get(position), 1, email, password);
                                }

                            });
                    alertDialogBuilder.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            finalViewHolder.image.setImageResource(R.drawable.no);
                            list.get(position).setEstado("N");
                            handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                            handler.updateRelacaoTrad(list.get(position), 0, email,password);
                        }

                    });
                    alertDialogBuilder.setNeutralButton("Pendente", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            finalViewHolder.image.setImageResource(R.drawable.unknown);
                            list.get(position).setEstado("I");
                            handler.updateAd(list.get(position).getNumero_anuncio(), list.get(position));
                            handler.updateRelacaoTrad(list.get(position), 0,email,password);
                        }

                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            });
//            view.setTag(viewHolder);
            viewHolder.image.setTag(list.get(position));
            if (convertView != null)
            {
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            view = convertView;
//            ((ViewHolder) view.getTag()).image.setTag(list.get(position));
        }

        if (list.get(position).getEstado().equals("Y")) {
            viewHolder.image.setImageResource(R.drawable.yes);
        } else if (list.get(position).getEstado().equals("N")) {
            viewHolder.image.setImageResource(R.drawable.no);
        } else {
            viewHolder.image.setImageResource(R.drawable.unknown);
        }

//        ViewHolder holder = (ViewHolder) view.getTag();
        viewHolder.ling_origem.setText("From: " + list.get(position).getLingua_origem());
        viewHolder.ling_destino.setText("To: " + list.get(position).getLingua_destino());
        viewHolder.num_palavras.setText("Words #: " + String.valueOf(list.get(position).getNumero_palavras()));
        viewHolder.valor.setText("Earn: " + String.valueOf(list.get(position).getValor()) + " €");
        viewHolder.data.setText("Start in: " + list.get(position).getData_inicio());
        viewHolder.dias.setText("Days #: " + String.valueOf(list.get(position).getNumero_dias()));
        viewHolder.software.setText("Software: " + list.get(position).getSoftware());
        viewHolder.icon.setBackgroundColor(list.get(position).getLogoColor());
        viewHolder.icon.getBackground().setAlpha(50); // queremos transparencia no logo para nao ficar com cores muito berrantes

        return convertView;
    }

}

