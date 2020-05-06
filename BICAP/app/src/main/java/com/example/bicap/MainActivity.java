package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://code.tutsplus.com/it/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true); // Se si è certi che le dimensioni del RecyclerView non cambieranno, è possibile aggiungere la seguente stringa per migliorare le prestazioni:

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        LinearLayout mainLinearLayout = (LinearLayout)findViewById(R.id.mainLinearLayout);



        IndaginiHeadList indaginiHeadList = getIndaginiHeadList();

        RVAdapter adapter = new RVAdapter(indaginiHeadList.getHeads());
        rv.setAdapter(adapter);
    }


    public IndaginiHeadList getIndaginiHeadList(){
        IndaginiHeadList indaginiHeadList = new IndaginiHeadList();
        List<IndagineHead> lista = new ArrayList<IndagineHead>();

        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/misc/navbar.svg?3", 830616));
        lista.add(new IndagineHead("Colori","Gianluca Quaglia", "https://mangadex.org/images/misc/navbar.svg?3", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/misc/navbar.svg?3", 830075));

        indaginiHeadList.setHeads(lista);

        return indaginiHeadList;
    }
}
