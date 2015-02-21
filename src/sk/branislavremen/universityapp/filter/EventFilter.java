package sk.branislavremen.universityapp.filter;

import java.util.ArrayList;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;
import android.widget.Filter;

public class EventFilter extends Filter {
	
	private ArrayList<EventData> myEventData;
	private ArrayList<EventData> newList;
	EventItemAdapter adapter;
	
	
	public void setAdapter(EventItemAdapter adapter) {
		this.adapter = adapter;
	}

	public ArrayList<EventData> getMyEventData() {
		return myEventData;
	}

	public void setMyEventData(ArrayList<EventData> myEventData) {
		this.myEventData = myEventData;
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
            results.values = myEventData;
            results.count = myEventData.size();
        }
        else {
            // Some search copnstraint has been passed
            // so let's filter accordingly
            ArrayList<EventData> filteredEvents = new ArrayList<EventData>();

            // We'll go through all the contacts and see
            // if they contain the supplied string
            for (EventData ed : myEventData) {
                if (ed.getEventTitle().toUpperCase().contains( constraint.toString().toUpperCase() )) {
                    // if `contains` == true then add it
                    // to our filtered list
                    filteredEvents.add(ed);
                }
            }

            // Finally set the filtered values and size/count
            results.values = filteredEvents;
            results.count = filteredEvents.size();
        }

        // Return our FilterResults object
        return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		 newList = (ArrayList<EventData>) results.values;
		 adapter.setItems(newList);
         adapter.notifyDataSetChanged();
		
	}

}
