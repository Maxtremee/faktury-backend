package com.pwr.faktury.models.adapters;

import com.pwr.faktury.model.Contractor;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contractors")
public class ContractorAdapter extends Contractor {
    Contractor contractor;

    public ContractorAdapter(Contractor contractor) {
        this.contractor = contractor;
    }

    public Contractor get() {
        return this.contractor;
    }
}
