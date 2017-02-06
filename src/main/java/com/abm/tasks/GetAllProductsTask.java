package com.abm.tasks;

import com.abm.adapters.Allegro;
import com.abm.adapters.DBA;
import com.abm.utils.Util;
import com.abm.webapi.ItemCatList;
import com.abm.webapi.ItemImageList;
import com.abm.webapi.ItemInfoStruct;
import com.abm.webapi.SellItemStruct;
import javafx.concurrent.Task;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by br33 on 03.02.2017.
 */
public class GetAllProductsTask extends Task<Integer> {
    private DBA dba = DBA.getInstance();
    private Allegro allegro = null;

    @Override
    protected Integer call() throws Exception {
        if (!dba.deleteAll("abm_products_working")) {
            return 1;
        }

        allegro = new Allegro();

        int totalProductCount = allegro.getItemsCount();
        ArrayList<Long> ids = new ArrayList<Long>();
        ArrayList<ArrayList<String[]>> paramsContainer = new ArrayList<ArrayList<String[]>>();
        ArrayList<SellItemStruct> items = null;

        int i = 0;
        boolean proceed = true;
        while (proceed) {
            updateProgress(Allegro.PORTION * i, totalProductCount);

            try {
                items = allegro.getSellItems(i);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

            if (items.size() <= 0) {
                proceed = false;
            } else {
                ids.clear();
                paramsContainer.clear();

                for (SellItemStruct item : items) {
                    ArrayList<String[]> params = new ArrayList<String[]>();
                    params.add(new String[]{"price", String.valueOf(item.getItemPrice()[0].getPriceValue())});
                    paramsContainer.add(params);

                    ids.add(item.getItemId());
                }
                items = null;

                paramsContainer = makeParamsFromItemsInfo(ids, paramsContainer);

                for (ArrayList<String[]> parameters : paramsContainer) {
                    if (!dba.addNewRow("abm_products_working", parameters)) {
                        return 2;
                    }
                }
            }

            i++;
        }

        return 0;
    }

    private ArrayList<ArrayList<String[]>> makeParamsFromItemsInfo(ArrayList<Long> ids, ArrayList<ArrayList<String[]>> paramsContainer) {
        ArrayList<ItemInfoStruct> itemsInfo = null;
        ArrayList<String[]> params = null;

        try {
            itemsInfo = allegro.getItemsInfo(Util.convert(ids));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int nr = 0;
        for (ItemInfoStruct itemInfo : itemsInfo) {
            String category = "";
            int i = 1;
            for (ItemCatList cat : itemInfo.getItemCats()) {
                category += cat.getCatName();
                if (i != itemInfo.getItemCats().length)
                    category += " > ";

                i++;
            }

            String image = "";
            ItemImageList[] allImages = itemInfo.getItemImages();
            for (ItemImageList img : allImages) {
                if (image == "" || img.getImageType() == 1)
                    image = img.getImageUrl();
            }

            params = paramsContainer.get(nr);
            params.add(new String[]{"id", String.valueOf(itemInfo.getItemInfo().getItId())});
            params.add(new String[]{"name", itemInfo.getItemInfo().getItName()});
            params.add(new String[]{"category", category});
            params.add(new String[]{"description", itemInfo.getItemInfo().getItDescription()});
            params.add(new String[]{"image", image});
            params.add(new String[]{"offers", String.valueOf(itemInfo.getItemInfo().getItBidCount())});

            nr++;
        }

        return paramsContainer;
    }

}
