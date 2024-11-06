package shopping.api.app;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class EbayAdapter extends RecyclerView.Adapter<EbayAdapter.ViewHolder> {

    private List<EbayModel> products;
    Activity activity;

    public EbayAdapter(List<EbayModel> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EbayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ebay,parent,false);
        return new EbayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EbayAdapter.ViewHolder holder, int position) {

        EbayModel product = products.get(position);
        if (product.getTitle().isEmpty()){
            String search = activity.getIntent().getStringExtra("search");
            holder.itemName.setText(search);
        }else{
            holder.itemName.setText(product.getTitle());

        }
        double dollar = 82.32;
        double value = product.getPrice() * dollar;
        String format = new DecimalFormat("##.##").format(value);
        holder.price.setText(String.format("₹%s", format));

        try {
            Picasso.get().load(product.getImage()).placeholder(R.color.purple_200)
                    .into(holder.itemImage);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("link",product.getProductLink());
                view.getContext().startActivity(intent);

                Toast.makeText(view.getContext(), product.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,price;
        ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
            itemImage = itemView.findViewById(R.id.itemImage);


        }
    }
}
