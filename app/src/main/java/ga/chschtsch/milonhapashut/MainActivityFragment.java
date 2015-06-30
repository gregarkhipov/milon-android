package ga.chschtsch.milonhapashut;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> mEntryAdapter;

    public MainActivityFragment() {
    }

    SearchView inputSearch;
    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] entryArray = {};

        Context context = getActivity();

        MyDbHelper db = new MyDbHelper(this, "milon.db", context, 1);

        final List<Word> wordList = db.getAllWords();

        List<String> queryEntries = new ArrayList<String>(
                Arrays.asList(entryArray)
        );

        mEntryAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_entry,
                R.id.list_item_entry_textview,
                queryEntries
        );

        listview = (ListView) rootView.findViewById(
                R.id.listview_entries
        );

        listview.setAdapter(mEntryAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String entry = mEntryAdapter.getItem(position);
                Toast.makeText(getActivity(), entry, Toast.LENGTH_SHORT).show();
            }
        });

        inputSearch = (SearchView) rootView.findViewById(
                R.id.inputSearch
        );

        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayAdapter<String> adapter = MainActivityFragment.this.mEntryAdapter;

                boolean wordFound = false;
                for (Word newWord: wordList) {
                    String t = newWord.getTranslated2();
                    String q = query.toLowerCase();
                    q = q.replaceAll("[\u0591-\u05C7]", "");
                    if (t.equals(q)) {
                        wordFound = true;
                        String translated = newWord.getTranslated();
                        String translation = newWord.getTranslation();
                        String part = newWord.getPart();
                        String result = MessageFormat.format("{0} - {1} ({2})", translated, translation, part);
                        adapter.insert(result, 0);
                    }
                }

                if (wordFound == false) {
                    Toast.makeText(getActivity(), getString(R.string.word_not_found), Toast.LENGTH_SHORT).show();
                }

                for(int i=0 ; i<adapter.getCount() ; i++){
                    Object obj = adapter.getItem(i);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return rootView;
    }
}
