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
    double lowest_recorded_price, highest_recorded_price;
    List<String> images;

}

class Offers {
    String merchant, domain, title, currency, condition, link;
    double price;
    Long updated_t;
}