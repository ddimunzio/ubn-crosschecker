/*
 * UBN Cross Checker - Amateur Radio Contest Log Analysis Tool
 *
 * Copyright (c) 2025 LW5HR
 *
 * This software is provided free of charge for the amateur radio community.
 * Feel free to use, modify, and distribute.
 *
 * @author LW5HR
 * @version 1.0
 * @since 2025
 *
 * 73!
 */
package org.lw5hr.repositories;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.lw5hr.model.Qso;

@Repository(forEntity = Qso.class)
public interface QsoRepository extends EntityRepository<Qso, Long> {
}
