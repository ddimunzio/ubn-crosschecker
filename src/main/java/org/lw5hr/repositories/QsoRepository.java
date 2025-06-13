package org.lw5hr.repositories;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.lw5hr.model.Qso;

@Repository(forEntity = Qso.class)
public interface QsoRepository extends EntityRepository<Qso, Long> {
}
