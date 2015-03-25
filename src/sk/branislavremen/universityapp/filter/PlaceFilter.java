package sk.branislavremen.universityapp.filter;

import java.util.ArrayList;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.adapter.PlaceItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;
import sk.branislavremen.universityapp.vo.PlaceData;
import android.widget.Filter;

public class PlaceFilter extends Filter {
	
	private ArrayList<PlaceData> myPlaceData;
	private ArrayList<PlaceData> newList;
	PlaceItemAdapter adapter;
	
	
	public void setAdapter(PlaceItemAdapter adapter) {
		this.adapter = adapter;
	}

	public ArrayList<PlaceData> getMyPlaceData() {
		return myPlaceData;
	}

	public void setMyPlaceData(ArrayList<PlaceData> myPlaceData) {
		this.myPlaceData = myPlaceData;
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		// Create a FilterResults object
        FilterResults results = new FilterResults();

        // If the constraint (search string/pattern) is null
        // or its length is 0, i.e., its empty then
        // we just set the `values` property to the
        // original contacts list which contains all of them
        if (constraint == null || constraint.length() == 0) {
            results.values = myPlaceData;
            results.count = myPlaceData.size();
        }
        else {
            // Some search copnstraint has been passed
            // so let's filter accordingly
            ArrayList<PlaceData> filteredPlaces = new ArrayList<PlaceData>();

            // We'll go through all the contacts and see
            // if they contain the supplied string
            for (PlaceData ed : myPlaceData) {
                if (ed.getTitle().toUpperCase().contains( constraint.toString().toUpperCase() )) {
                    // if `contains` == true then add it
                    // to our filtered list
                    filteredPlaces.add(ed);
                }
            }

            // Finally set the filtered values and size/count
            results.values = filteredPlaces;
            results.count = filteredPlaces.size();
        }

        // Return our FilterResults object
        return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		 newList = (ArrayList<PlaceData>) results.values;
		 adapter.setItems(newList);
         adapter.notifyDataSetChanged();
		
	}

}
