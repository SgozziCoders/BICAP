package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndagineAdapter.OnCardListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://code.tutsplus.com/it/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true); // Se si è certi che le dimensioni del RecyclerView non cambieranno, è possibile aggiungere la seguente stringa per migliorare le prestazioni:

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();

        IndagineAdapter adapter = new IndagineAdapter(indaginiHeadList.getHeads(), this);
        rv.setAdapter(adapter);
    }

    //PROVVISORIO
    public IndaginiHeadList getIndaginiHeadList(){
        IndaginiHeadList indaginiHeadList = new IndaginiHeadList();
        List<IndagineHead> lista = new ArrayList<IndagineHead>();

        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/misc/navbar.svg?3", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/misc/navbar.svg?3", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/misc/navbar.svg?3", 830075));

        indaginiHeadList.setHeads(lista);

        return indaginiHeadList;
    }

    @Override
    public void onCardClick(int position) {
        String titolo = getIndaginiHeadList().getHeads().get(position).getTitoloIndagine(); //PROVVISORIO
        IndagineHead indagineHead = getIndaginiHeadList().getHeads().get(position);
        Intent intent = new Intent(MainActivity.this, IndagineActivity.class);
        intent.putExtra("Indagine", indagineHead);
        startActivity(intent);
    }
}
