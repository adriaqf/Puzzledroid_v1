package com.sds.puzzledroid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

//un BaseAdapter puede usarse para un Adapter en un listview o gridview
// hay que implementar algunos métodos heredados de la clase Adapter,
//porque BaseAdapter es una subclase de Adapter
// estos métodos en este ejemplo son:  getCount(), getItem(), getItemId(), getView()
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // el constructor necesita el contexto de la actividad donde se utiliza el
    // adapter
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {// devuelve el número de elementos que se introducen
        // en el adapter
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        // este método debería devolver el objeto que esta en esa posición del
        // adapter. No es necesario en este caso más que devolver un objeto null.
        return null;
    }

    public long getItemId(int position) {
        // este método debería devolver el id de fila del item que esta en esa
        // posición del adapter. No es necesario en este caso más que devolver 0.
        return 0;
    }

    // crear un nuevo ImageView para cada item referenciado por el Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // este método crea una nueva View para cada elemento añadido al
        // com.sds.puzzledroid.ImageAdapter. Se le pasa el View en el que se ha pulsado, converview
        // si convertview es null, se instancia y configura un ImageView con las
        // propiedades deseadas para la presentación de la imagen
        // si converview no es null, el ImageView local es inicializado con este
        // objeto View
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));// ancho
            // y alto
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //la imagen de este imageView será el elemento de la
        //posicion 'position' del vector mThumbIds, declarado abajo.
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    // en un array de Integer se guardan los números que son los id de todos los
    // recursos guardados en drawable
    // dentro de la carpeta res/drawable están todas las imágenes con esos
    // nombres. con su número entero se les puede identificar como recurso
    private Integer[] mThumbIds = { R.drawable.imagen1, R.drawable.imagen2,
            R.drawable.imagen3, R.drawable.imagen4, R.drawable.imagen5,
            R.drawable.imagen6 };
}