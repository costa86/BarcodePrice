package com.example.barcodeprice;

import java.util.List;

public class ResultsModel {
    String code;
    int total, offset;
    List<Item> items;
}


class Item {
    String ean, title, description;
    List<Offers> offers;

}

class Offers {
    String merchant, domain, title;
}