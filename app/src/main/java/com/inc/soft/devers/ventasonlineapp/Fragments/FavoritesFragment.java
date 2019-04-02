package com.inc.soft.devers.ventasonlineapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inc.soft.devers.ventasonlineapp.Adapters.PostAdapter;
import com.inc.soft.devers.ventasonlineapp.Models.Post;
import com.inc.soft.devers.ventasonlineapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView favoritePostRecyclerView ;
    PostAdapter favoritePostAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Post> favoritePostList;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);
        favoritePostRecyclerView  = fragmentView.findViewById(R.id.favoritePostRV);
        favoritePostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoritePostRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Articulos publicados");
        return fragmentView ;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoritePostList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {

                    Post post = postsnap.getValue(Post.class);
                    favoritePostList.add(post);


                }

                favoritePostAdapter = new PostAdapter(getActivity(),favoritePostList);
                favoritePostRecyclerView.setAdapter(favoritePostAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
