package com.github.hronom.scrape.dat.rooms.core.html.parsers.pojos;

import java.util.ArrayList;

public class Motel6JsonAvailabilityPojo {
    public String property_name;
    public String property_id;
    public boolean show_directbill_allowed;
    public boolean show_directbill_not_allowed;
    public ArrayList<Motel6JsonAvailableRatePojo> best_available_rates;
    public ArrayList<Motel6JsonDiscountCodePojo> restricted_discount_code;
}
