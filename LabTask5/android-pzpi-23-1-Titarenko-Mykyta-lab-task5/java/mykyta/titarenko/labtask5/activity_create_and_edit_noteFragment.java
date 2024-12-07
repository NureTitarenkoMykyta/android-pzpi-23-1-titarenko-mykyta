package mykyta.titarenko.labtask5;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mykyta.titarenko.labtask5.databinding.FragmentActivityCreateAndEditNoteBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class activity_create_and_edit_noteFragment extends Fragment {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            Activity activity = getActivity();
            if (activity != null
                    && activity.getWindow() != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    };
    private View mContentView;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private FragmentActivityCreateAndEditNoteBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentActivityCreateAndEditNoteBinding.inflate(inflater, container, false);
        mContentView = binding.getRoot();
        return binding.getRoot();
    }
    ImageView iconImageView;
    List<Note> notes;
    byte[] imageBytes;
    Notes Notes;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVisible = true;
        Notes = ((NotesApplication) requireActivity().getApplication()).GetNotes();
        Button createOrEditButton = view.findViewById(R.id.createOrEditButton);
        EditText titleEditTextButton = view.findViewById(R.id.titleEditText);
        EditText descriptionEditTextButton = view.findViewById(R.id.descriptionEditText);
        Spinner importanceSpinner = view.findViewById(R.id.importanceSpinner);
        List<ImportanceItem> importanceItems = new ArrayList<>();
        importanceItems.add(new ImportanceItem(getString(R.string.lowImportance), R.drawable.not_important_icon));
        importanceItems.add(new ImportanceItem(getString(R.string.mediumImportance), R.drawable.important_icon));
        importanceItems.add(new ImportanceItem(getString(R.string.highImportance), R.drawable.very_important_icon));
        ImportanceAdapter importanceAdapter = new ImportanceAdapter(requireContext(), importanceItems);
        importanceSpinner.setAdapter(importanceAdapter);
        EditText editTextDate = view.findViewById(R.id.editTextDate);
        EditText editTextTime = view.findViewById(R.id.editTextTime);
        Button chooseImageButton = view.findViewById(R.id.chooseImageButton);
        Button cancleButton = view.findViewById(R.id.cancleButton);
        iconImageView = view.findViewById(R.id.iconImageView);
        notes = Notes.getNotes();

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (getActivity().getIntent().getBooleanExtra("view", false) || getActivity().getIntent().getBooleanExtra("edit", false)){
            Note note = notes.get(getActivity().getIntent().getIntExtra("index", 0));
            importanceSpinner.setSelection(note.getImportance());
            titleEditTextButton.setText(note.getTitle());
            descriptionEditTextButton.setText(note.getDescription());
            editTextDate.setText(note.getEventDate());
            editTextTime.setText(note.getEventTime());
            imageBytes = NoteAdapter.StandartIconCheck(note.getImageData(), requireContext());
            iconImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            if (getActivity().getIntent().getBooleanExtra("view", false)){
                titleEditTextButton.setEnabled(false);
                descriptionEditTextButton.setEnabled(false);
                descriptionEditTextButton.setHint("");
                editTextDate.setEnabled(false);
                editTextDate.setHint("");
                editTextTime.setEnabled(false);
                editTextTime.setHint("");
                removeTransparency(titleEditTextButton);
                removeTransparency(descriptionEditTextButton);
                removeTransparency(editTextDate);
                removeTransparency(editTextTime);
                chooseImageButton.setVisibility(View.INVISIBLE);
                importanceSpinner.setEnabled(false);
                createOrEditButton.setVisibility(View.INVISIBLE);
            } else {
                createOrEditButton.setText(getString(R.string.edit));
            }
        }

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        createOrEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditTextButton.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle(R.string.incorrectInput);
                    builder.setMessage(R.string.noTitle);
                    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    return;
                }

                if (getActivity().getIntent().getBooleanExtra("edit", false)){
                    Note note = notes.get(getActivity().getIntent().getIntExtra("index", 0));
                    Notes.EditNote( note.getNumber(),
                            titleEditTextButton.getText().toString(),
                            descriptionEditTextButton.getText().toString(),
                            importanceSpinner.getSelectedItemPosition(),
                            editTextDate.getText().toString(),
                            editTextTime.getText().toString(),
                            note.getCreationDate(),
                            imageBytes);
                } else {
                    Notes.AddNote(titleEditTextButton.getText().toString(),
                            descriptionEditTextButton.getText().toString(),
                            importanceSpinner.getSelectedItemPosition(),
                            editTextDate.getText().toString(),
                            editTextTime.getText().toString(),
                            dayOfMonth + "." + month + "." + year,
                            imageBytes);
                }
                Activity activity = getActivity();
                if (activity != null) {
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, hourOfDay, minute, true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            iconImageView.setImageURI(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeTransparency(TextView textView){

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        delayedHide(100);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            // Clear the systemUiVisibility flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentView = null;
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
