package com.abm.adapters;

import com.abm.models.ConnectionParameter;
import com.abm.webapi.*;
import com.abm.webapi.holders.ArrayOfItemsIDHolder;
import com.abm.webapi.holders.ArrayOfSellItemStructHolder;
import com.abm.webapi.holders.ArrayOfStructItemInfoListHolder;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.LongHolder;
import javax.xml.rpc.holders.StringHolder;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by br33 on 02.02.2017.
 */
public class Allegro {
    public static final int PORTION = 25;

    private String username;
    private String password;
    private String apiKey;

    private AllegroWebApiPortType port;
    private StringHolder sessionHandlePart;

    public Allegro() {
        this.username = ConnectionParameter.connected.getUsername();
        this.password = ConnectionParameter.connected.getPassword();
        this.apiKey = ConnectionParameter.connected.getApiKey();

        // Make a service
        AllegroWebApiServiceLocator service = new AllegroWebApiServiceLocator();

        // Now use the service to get a stub which implements the SDI.
        try {
            port = service.getAllegroWebApiPort();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        // Make the actual call
        final String webapiKey = apiKey;
        final int countryCode = 1;
        long localVerKey = -1;

        StringHolder info = new StringHolder();
        LongHolder currentVerKey = new LongHolder();

        System.out.println("Receving key version... ");
        try {
            port.doQuerySysStatus(1, countryCode, webapiKey, info, currentVerKey);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("done. Current version key=" + currentVerKey.value);

        if (localVerKey != currentVerKey.value) {
            System.out.println("Warning: key versions don't match!");
            localVerKey = currentVerKey.value;
        }

        sessionHandlePart = new StringHolder();
        LongHolder userId = new LongHolder();
        LongHolder serverTime = new LongHolder();
        System.out.println("Logging in... ");
        try {
            port.doLogin(username, password, countryCode, apiKey, localVerKey, sessionHandlePart, userId, serverTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("done.");
    }

    public ArrayList<SellItemStruct> getSellItems(int page) throws RemoteException {
        ArrayOfSellItemStructHolder sellItems = new ArrayOfSellItemStructHolder();

        port.doGetMySellItems(sessionHandlePart.value,
                new SortOptionsStruct(3, 1),
                null,
                null,
                0,
                null,
                PORTION,
                page,
                new IntHolder(),
                sellItems
        );

        ArrayList<SellItemStruct> items = new ArrayList<SellItemStruct>();
        for (SellItemStruct item : sellItems.value) {
            items.add(item);
        }

        return items;
    }

    public ArrayList<ItemInfoStruct> getItemsInfo(long[] ids) throws RemoteException {
        ArrayOfStructItemInfoListHolder itemsHolder = new ArrayOfStructItemInfoListHolder();

        port.doGetItemsInfo(sessionHandlePart.value,
                ids, 1, 1, 1, 0, 0, 0, itemsHolder,
                new ArrayOfItemsIDHolder(), new ArrayOfItemsIDHolder()
                );

        ArrayList<ItemInfoStruct> items = new ArrayList<ItemInfoStruct>();
        for (ItemInfoStruct item : itemsHolder.value) {
            items.add(item);
        }

        return items;
    }

}
