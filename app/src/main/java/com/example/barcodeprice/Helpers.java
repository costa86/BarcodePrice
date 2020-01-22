package com.example.barcodeprice;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ShareCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contém métodos com funcionalidades de apoio para outras classes
 */
public class Helpers {


    /**
     * Ćonverte data recebida em segundos (epoch timestamp 10 digitos) para um formato desejado
     *
     * @param seconds
     * @param pattern
     * @return
     */
    static String secondsToDate(long seconds, String pattern) {
        Date date = new Date(seconds * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat(pattern);
        return jdf.format(date);
    }



    /**
     * Cria opção de partilhar conteudo
     *
     * @param context
     * @param title
     * @param text
     */
    static void shareThis(Context context, String title, String text) {
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from((Activity) context)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(text)
                .startChooser();
    }
}
