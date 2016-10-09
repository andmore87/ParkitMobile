package com.andmore.parkitmobile.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.andmore.parkitmobile.activity.R;
import com.andmore.parkitmobile.activity.ReservePlaceActivity;
import com.andmore.parkitmobile.entity.Local;
import com.andmore.parkitmobile.image.util.GridLocalInfoAdapter;
import com.andmore.parkitmobile.image.util.GridNodoAdapter;
import com.andmore.parkitmobile.repository.SQLiteSeccionHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


@SuppressLint("NewApi")
public class SectionContentFragment extends android.support.v4.app.Fragment {

	Point p;
	GridView gridNodo;
	GridView gridInfoLocal;
	private AdView mAdView;
	
	private SQLiteSeccionHandler dbSeccion;

	String[] txtsNodoName = { "Verde", "Amarrillo", "Rojo", "Naranja", "Plata",
			"Oro" };
	String[] txtsQtyNumber = { "2", "30", " ", "50", " ", "5" };
	int[] imageId = { R.drawable.freeplace, R.drawable.freeplace,
			R.drawable.emptyplace, R.drawable.freeplace, R.drawable.emptyplace,
			R.drawable.disabled };

	String[] txtsLocalName; // = { "Adidas", "Nike", "Banco ABC", "Banco Av",
			//"ilForno", "jeans v" };
	String[] txtsLocalNumber ;//= { "Local: 1-01", "Local: 1-02", "Local: 1-03",
			//"Local: 1-15", "Local: 1-17", "Local: 1-28" };
	int[] imageLocalInfoId = { R.drawable.localadidas, R.drawable.stub,
			R.drawable.stub, R.drawable.localadidas, R.drawable.stub,
			R.drawable.stub };
	String[] txtsLocalDesc;
	String[] txtsLocalTel;
	String[] txtsLocalURL;
	String[] txtsLocalCateg;

	public SectionContentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		dbSeccion = new SQLiteSeccionHandler(getContext());
		int seccionId = 0;
		Bundle extras = this.getArguments();
		if (extras != null) {
			seccionId = extras.getInt("seccionId");
		}
		
		List<Local> localList = dbSeccion.getLocalsBySeccion(seccionId);
		if(localList != null){
			  txtsLocalName = new String[localList.size()];
			  txtsLocalNumber = new String[localList.size()];
			  txtsLocalDesc = new String[localList.size()];
			  txtsLocalTel = new String[localList.size()];
			  txtsLocalURL = new String[localList.size()];
			  txtsLocalCateg = new String[localList.size()];
			  
			  //imageLocalInfoId = new int[localList.size()];
			for (int i = 0; i < localList.size(); i++) {
				Local localObject = localList.get(i);
				txtsLocalName [i] = localObject.getNombre(); 
				txtsLocalNumber [i] = "Local: "+localObject.getNumero(); 
				txtsLocalDesc [i] = localObject.getDescripcion(); 
				txtsLocalTel [i] = localObject.getTelefono();
				txtsLocalURL [i] =localObject.getUrl();
				txtsLocalCateg [i] =localObject.getCategoria();
				//imageLocalInfoId [i] = localObject.getFoto(); 
			}
			
		}
		
		 

		View rootView = inflater.inflate(R.layout.fragment_section_content,
				container, false);

		GridNodoAdapter adapter = new GridNodoAdapter(getActivity(),
				txtsNodoName, txtsQtyNumber, imageId);
		gridNodo = (GridView) rootView.findViewById(R.id.gridNodo);
		gridNodo.setAdapter(adapter);

		GridLocalInfoAdapter adapterLocalInfo = new GridLocalInfoAdapter(
				getActivity(), txtsLocalName, txtsLocalNumber, imageLocalInfoId);
		gridInfoLocal = (GridView) rootView.findViewById(R.id.gridInfoLocal);
		gridInfoLocal.setAdapter(adapterLocalInfo);

		gridNodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (id == 3) {
					Toast.makeText(
							getActivity(),
							"Actualmente no hay plazas disponibles en esta sector.",Toast.LENGTH_LONG).show();
	 			}else{
	 				 Intent i = new Intent(getActivity(), ReservePlaceActivity.class);
					 i.putExtra("Id", position);
					 startActivity(i);
	 			}

			}
		});

		gridInfoLocal
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
		 

					/*	LinearLayout lyContent = new LinearLayout(getActivity());
						lyContent.setOrientation(LinearLayout.VERTICAL);

						final TextView txtInfoWithPhone = new TextView(
								getActivity());
						final SpannableString strInfoWithPhone = new SpannableString(
								""
										+ txtsLocalNumber[position]
										+ ".\n"
										+ "Categoria: Tienda Deportiva.\n"
										+ "Tel:6058474\n"
										+ "Descubre la gran variedad de productos que la tienda oficial "
										+ "de adidas Colombia tiene para ti. "
										+ "Compra zapatos, ropa y mucho m�s.\n");
						Linkify.addLinks(strInfoWithPhone,
								Linkify.PHONE_NUMBERS);
						txtInfoWithPhone.setText(strInfoWithPhone);
						lyContent.addView(txtInfoWithPhone);

						final TextView txtInfoWithURL = new TextView(
								getActivity());
						final SpannableString strInfoWithURL = new SpannableString(
								"http://www.adidas.co/");
						Linkify.addLinks(strInfoWithURL, Linkify.WEB_URLS);
						txtInfoWithURL.setText(strInfoWithURL);
						lyContent.addView(txtInfoWithURL);
					 	*/
					 
						
						LayoutInflater inflater = getActivity().getLayoutInflater(); 
						View dialogView = inflater.inflate(
								R.layout.alertdialg_section_localinfo, null);
						
						mAdView = (AdView) dialogView.findViewById(R.id.adView);
				        AdRequest adRequest = new AdRequest.Builder()
				                .build();
				        mAdView.loadAd(adRequest);
						
						TextView txtNumLocal = (TextView) dialogView
								.findViewById(R.id.alertdialTxtNumLocal);
						
						TextView txtCategory = (TextView) dialogView
								.findViewById(R.id.alertdialTxtCategory);
						
						TextView txtPhone = (TextView) dialogView
								.findViewById(R.id.alertdialTxtPhone);
						
						TextView txtDesc = (TextView) dialogView
								.findViewById(R.id.alertdialTxtDesc);
						
						TextView txtUrl = (TextView) dialogView
								.findViewById(R.id.alertdialTxtUrl);
						
						txtNumLocal.setText(txtsLocalNumber[position]);
						txtCategory.setText("Categoria: "+txtsLocalCateg[position]);
						txtPhone.setText("Tel: "+txtsLocalTel[position]);
						txtDesc.setText(txtsLocalDesc[position]);
						txtUrl.setText(txtsLocalURL[position]);
				
						AlertDialog alertDialog = new AlertDialog.Builder(
								getActivity()).create();
						alertDialog.setTitle(txtsLocalName[position]);
						alertDialog.setIcon(imageLocalInfoId[position]);
						alertDialog.setView(dialogView);
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
								"OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alertDialog.show();

					}
				});

		return rootView;
	}

}
