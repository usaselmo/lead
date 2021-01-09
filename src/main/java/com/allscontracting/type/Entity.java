package com.allscontracting.type;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {

	ID getId();

}
