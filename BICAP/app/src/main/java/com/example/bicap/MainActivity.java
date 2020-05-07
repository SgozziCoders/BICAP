package com.example.bicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndagineAdapter.OnCardListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://code.tutsplus.com/it/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        RecyclerView rv = (RecyclerView)findViewById(R.id.indaginiRecycleView);
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

        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/avatars/default2.jpg", 830075));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));
        lista.add(new IndagineHead("Droghe","Alessio Villani", "https://mangadex.org/images/avatars/default2.jpg", 830075));
        lista.add(new IndagineHead("Colori","Michele Rago", "https://mangadex.org/images/avatars/default2.jpg", 830616));
        lista.add(new IndagineHead("Alimenti","Gianluca Quaglia", "https://mangadex.org/images/avatars/default2.jpg", 829533));


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bicap_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                //Intent contattaci = new Intent(MainActivity.this, Contattaci.class);
                //startActivity(contattaci);
                return true;
            case R.id.item2:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
