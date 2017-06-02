package iit.valentinpichavant.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class OfficeAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "OfficeAdapter";
    private List<Office> officesList;
    private MainActivity mainAct;

    public OfficeAdapter(List<Office> officesList, MainActivity ma) {
        this.officesList = officesList;
        mainAct = ma;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gov_memb_item, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Office office = officesList.get(position);
        System.out.println("Office " + office);
        holder.officeName.setText(office.getName());
        System.out.println("Position " + position);
        Official official = office.getOfficial();
        if (official.getOfficialIndices() == position) {
            String text;
            if (official.getParty() != null) {
                text = official.getName() + " (" + official.getParty() + ")";
            } else {
                text = official.getName() + " (Unknown)";
            }
            holder.mame.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return officesList.size();
    }
}
