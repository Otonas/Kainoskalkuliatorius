package lt.petabitas.kainoskalkuliatorius;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import lt.petabitas.kainoskalkuliatorius.R;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {

    private List<FavoriteListItem> favorites;
    private Context context;

    public FavoriteAdapter(List<FavoriteListItem> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    public void setFavorites(List<FavoriteListItem> favorites) {
        this.favorites = favorites;
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
        FavoriteListItem item = (FavoriteListItem) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.favorite_text);
        textView.setText(item.getPavadinimas() + " | " + item.getRusis() + " | " + item.getBePVM() + " | " + item.getKaina());
        return view;
    }


}


