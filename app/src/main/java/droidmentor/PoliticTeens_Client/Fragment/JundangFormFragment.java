package droidmentor.PoliticTeens_Client.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenfrvr.hashtagview.HashtagView;

import java.util.Arrays;
import java.util.List;

import droidmentor.PoliticTeens_Client.GetUserId;
import droidmentor.PoliticTeens_Client.models.Jundang;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.S;

/**
 * A simple {@link Fragment} subclass.
 */
public class JundangFormFragment extends Fragment {

    private DatabaseReference databaseReference ;
    private static final String REQUIRED = "입력해주세요";
    HashtagView hashtagView2;
    public static final List<String> DATA = Arrays.asList("android", "library", "collection",
            "hashtags", "min14sdk", "UI", "view", "github", "opensource", "project", "widget");

    FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    FragmentTransaction tran;  //실제로 Fragment를 추가/삭제/재배치 하는 클래스의 참조변수

    String color;
    String just_color;
    EditText just_name,just_idea;
    Button register;
    ImageView selected_color;
    int just_count;
    boolean userhasId;

    public JundangFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jundang_form, container, false);
        just_name = (EditText) view.findViewById(R.id.just_name);
        just_idea = (EditText) view.findViewById(R.id.just_idea);
        register = (Button) view.findViewById(R.id.register);
        selected_color = (ImageView) view.findViewById(R.id.selected_color);

        manager = (FragmentManager) getFragmentManager();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        hashtagView2 = (HashtagView) view.findViewById(R.id.hashtags2);
        hashtagView2.setData(DATA, new HashtagView.DataTransform<String>() {
            @Override
            public CharSequence prepare(String item) {
                SpannableString spannableString = new SpannableString("#" + item);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#26303D")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                just_count = dataSnapshot.child("club_count").getValue(int.class);
                try {
                    if (dataSnapshot.child("users").child(GetUserId.getUid()).child("club").getValue(int.class) > 0 ) {
                        userhasId = true;
                    }
                    Log.d("ds1", just_count + "");
                }catch (NullPointerException e){
                    userhasId = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getActivity();

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("컬러피커~~!")
                        .initialColor(0xffffffff)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                // Handle on color change
                                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                                color = Integer.toHexString(selectedColor);
                            }
                        })
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Toast.makeText(getContext(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                                color = Integer.toHexString(selectedColor);
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_SHORT).show();

                                    selected_color.setBackgroundColor(Color.parseColor("#"+color));
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright))
                        .build()
                        .show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userhasId == true){
                    Toast.makeText(getContext(),"이미 가입된 정당이 있습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(just_name.getText().toString())) {
                    just_name.setError(REQUIRED);
                    return;
                } else if (TextUtils.isEmpty(just_idea.getText().toString())) {
                    just_idea.setError(REQUIRED);
                    return;
                } else if(color=="ffffff2e"){
                    Toast.makeText(getActivity(),"정당의 색을 지정해주세요!",Toast.LENGTH_SHORT).show();
                    return;
                }else if((hashtagView2.getSelectedItems().toString().equals("[]"))){
                    Toast.makeText(getActivity(),"정당의 분류를 지정해주세요!",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String name, idea, color2, category;
                    name = just_name.getText().toString();
                    idea = just_idea.getText().toString();
                    color2 = color;
                    category = hashtagView2.getSelectedItems().toString();
                    Log.d("ds0",just_count+"");


                    Log.d("ds2",just_count+"");
                    Jundang jungdang = new Jundang(name,S.userName, category,idea, color2);
                    databaseReference.child("club").child(""+just_count).setValue(jungdang);
                    String id = GetUserId.getUid();
                    databaseReference.child("users").child(id).child("club").setValue(just_count);
                    databaseReference.child("club_count").setValue(just_count+1);

                    tran = manager.beginTransaction();
                    Fragment frag = new MyJungdangFragment();
                    manager.beginTransaction().remove(JundangFormFragment.this).commit();
                    tran.replace(R.id.my_jungdang_layout, frag);
                    tran.commit();

                    return;

                }
            }
        });

        return view;
        }
    }
