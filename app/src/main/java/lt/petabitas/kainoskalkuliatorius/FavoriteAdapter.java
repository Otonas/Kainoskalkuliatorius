package lt.petabitas.kainoskalkuliatorius;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {

    private List<FavoriteList> favorites;
    private Context context;
    private Kaina kaina;

    public FavoriteAdapter(List<FavoriteList> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.favorite_item, null);
        FavoriteList item = (FavoriteList) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.favorite_text);
        textView.setText(item.getPavadinimas() + " | " + item.getRusis() + " | " + item.getBePVM() + " | " + item.getKaina());
        return view;
    }


}


