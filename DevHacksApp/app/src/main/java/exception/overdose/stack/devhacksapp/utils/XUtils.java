package exception.overdose.stack.devhacksapp.utils;

import android.content.Context;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.database.FoodDataSource;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by Adriana on 21/11/2015.
 */
public class XUtils {

    public static String getSubordersRepresentation(ArrayList<SubOrder> subOrderArrayList, Context context) {
        FoodDataSource foodDataSource = new FoodDataSource(context);
        foodDataSource.open();
        StringBuilder subordersStringBuilder = new StringBuilder();
        for (int i = 0; i < subOrderArrayList.size(); i++) {
            subordersStringBuilder.append(
                    subOrderArrayList.get(i).getQuantity() +
                    " x " + foodDataSource.getFood(subOrderArrayList.get(i).getFoodID()).getName());
            if (i != subOrderArrayList.size()-1)
                subordersStringBuilder.append(", ");
        }
        foodDataSource.closeHelper();
        return subordersStringBuilder.toString();
    }
}
