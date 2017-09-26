package droidmentor.PoliticTeens_Client.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import droidmentor.PoliticTeens_Client.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyJungdangFragment_Null extends Fragment {

    FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    FragmentTransaction tran;  //실제로 Fragment를 추가/삭제/재배치 하는 클래스의 참조변수
    Button makingJundang;

    public MyJungdangFragment_Null() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_myjungdangs_null, container, false);

        manager = (FragmentManager) getFragmentManager();

        //SegmentedButtonGroup myjundang_buttonGroup = (SegmentedButtonGroup) rootView.findViewById(R.id.myjundang_buttonGroup);
        /*myjundang_buttonGroup.setOnClickListener( new SegmentedButtonGroup.OnClickedButtonPosition(){
            @Override
            public  void onClickedButtonPosition(){

            }
        });*/
        TextView search=(TextView)view.findViewById(R.id.search_jungdang);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tran = manager.beginTransaction();
                Fragment frag = new MyJungdangFragment_SearchJungdang();
                tran.replace(R.id.my_jungdang_layout, frag);
                tran.addToBackStack(null);
                tran.commit();
            }
        });
        makingJundang=(Button)view.findViewById(R.id.makingJundang);
        makingJundang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tran = manager.beginTransaction();
                Fragment frag = new JundangFormFragment();
                tran.replace(R.id.my_jungdang_layout,frag);
                tran.commit();
            }
        });

        return view;

    }

}
