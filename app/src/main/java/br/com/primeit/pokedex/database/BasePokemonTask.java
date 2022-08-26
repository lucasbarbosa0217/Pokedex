package br.com.primeit.pokedex.database;

import android.os.AsyncTask;

public abstract class BasePokemonTask extends AsyncTask<Void, Void, Void> {
    private final FinalizadaListener listener;

    public BasePokemonTask(FinalizadaListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        listener.quandoFinalizada();
    }

    public interface FinalizadaListener{
        void quandoFinalizada();
    }
}
