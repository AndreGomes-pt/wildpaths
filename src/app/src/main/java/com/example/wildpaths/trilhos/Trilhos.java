package com.example.wildpaths.trilhos;

import static com.example.wildpaths.R.id;
import static com.example.wildpaths.R.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wildpaths.MainActivity;
import com.example.wildpaths.adapters.TrilhoAdapter;
import com.example.wildpaths.trilhos.selecionado.TrilhoSelecionado;


public class Trilhos extends Fragment {

    public boolean created = false;

    public static RecyclerView rvTrilhos;

    //Fragemnt para a troca da pagina dos trilhos para o trilho selecionado
    private static Fragment pagTrilho;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trilhos() {
    }


    // TODO: Rename and change types and number of parameters
    public static Trilhos newInstance(String param1, String param2) {
        Trilhos fragment = new Trilhos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.fragment_trilhos, container, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvTrilhos =view.findViewById(id.recycler);
                        rvTrilhos.removeAllViews();
                        //Cria os trilhos no recycle view e usa os dados retornados pelo metodo trilhosExemplo da classe Trilhos para prencher as informações dos trilhos
                        new Thread(new Runnable() {
                            @Override
                            public void run () {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rvTrilhos.setAdapter(new TrilhoAdapter(0, (byte) 0));
                                    }
                                });
                            }
                        }).start();

                        rvTrilhos.setVerticalScrollBarEnabled(false);

                        //Hide keyboard on touch
                        RelativeLayout relativeLayout = view.findViewById(id.pagTrilhos_holder);
                        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch (View view, MotionEvent motionEvent){
                                if (view == relativeLayout && motionEvent.getAction() == MotionEvent.ACTION_DOWN || view == rvTrilhos && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    MainActivity.imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                                }
                                return false;
                            }
                        });

                        //Refresh dos Trilhos
                        SwipeRefreshLayout refreshLayout = view.findViewById(id.swipeRefresh);
                        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh () {
                                TrilhoAdapter trilhoAdapter = new TrilhoAdapter(0, (byte) 0);
                                rvTrilhos.setAdapter(trilhoAdapter);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (trilhoAdapter.getUpdateStatus() == false) {
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        refreshLayout.setRefreshing(false);
                                    }
                                }).start();
                            }
                        });
                    }
                });
                setCreated(true);
        }
            public synchronized void setCreated(boolean value) {
                created = value;
            }
    }).start();
        while(created == false){
            //Colocar animação para indicar que esta a carregar os trilhos

        }
        return view;
    }

    //Mudança para a pg do trilho q foi clicado
    public static void trilhoSelecionado(View trilho, FragmentTransaction fragmentTransaction){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //cria o fragment do trilho selecionado
                pagTrilho = new TrilhoSelecionado(rvTrilhos.getChildLayoutPosition(trilho));
                fragmentTransaction.replace(id.trilhoSelecionadoHolder, pagTrilho);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //coloca o rv dos trilhos invisivel
                rvTrilhos.setVisibility(View.INVISIBLE);
            }
        }).start();
    }
}










