package technited.dota2central;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NotesAlertDialog extends DialogFragment{
	
	private TextView addNoteLabel;
	private EditText noteContent;
	private ImageView accept, cancel;
	private Typeface tf;
	private String fontPath2 = "fonts/segoeuil.ttf";
	private String noteContentString;
	
	public interface NotesDialogListener{
		public void onNoteAccepted(String newNoteContent);
        public void onNoteCancelled();
	}
	
	NotesDialogListener mNotesDialogListener;
	
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Build the dialog and set up the button click handlers
		 
		 noteContentString = getArguments().getString("noteContent");
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        View notesDialogView = inflater.inflate(R.layout.notes_window, null);
	        builder.setView(notesDialogView);
	        
	        
	        accept = (ImageView) notesDialogView.findViewById(R.id.notesView_bottomButtons_Accept);
	        
	        accept.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					mNotesDialogListener.onNoteAccepted(noteContent.getText().toString());
				}
			});
	        cancel = (ImageView) notesDialogView.findViewById(R.id.notesView_bottomButtons_Cancel);
	        
	        cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					mNotesDialogListener.onNoteCancelled();
				}
			});
	        addNoteLabel = (TextView) notesDialogView.findViewById(R.id.notesView_addNoteLabel);
	        noteContent = (EditText) notesDialogView.findViewById(R.id.notesView_noteContent);
	        tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath2);
	        addNoteLabel.setTypeface(tf);
	        noteContent.setTypeface(tf);
	        noteContent.setText(noteContentString);
	        
	        
	        return builder.create();
	}
	 
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mNotesDialogListener = (NotesDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	 }
}
