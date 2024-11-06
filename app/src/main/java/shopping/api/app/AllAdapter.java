package shopping.api.app;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.ViewHolder> {


    private List<AllModel> allModels;
    Activity activity;

    public AllAdapter(List<AllModel> products, Activity activity) {
        this.allModels = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all,parent,false);
        return new AllAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAdapter.ViewHolder holder, int position) {

        AllModel model = allModels.get(position);
        holder.itemName.setText(model.getTitle());

        double dollar = 82.32;
        double value = model.getExtracted_price() * dollar;

        String format = new DecimalFormat("##.##").format(value);
        holder.price.setText(String.format("₹%s", format));

        holder.text_sorce.setText(model.getSource());

        try {
            Picasso.get().load(model.getThumbnail()).placeholder(R.color.purple_700)
                    .into(holder.itemImage);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("link",model.getLink());
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return allModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,price,text_sorce;
        ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
            itemImage = itemView.findViewById(R.id.itemImage);
            text_sorce = itemView.findViewById(R.id.textSource);



        }
    }

}
