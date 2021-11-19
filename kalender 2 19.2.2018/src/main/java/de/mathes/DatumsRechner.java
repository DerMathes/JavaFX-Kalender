package de.mathes;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;


public class DatumsRechner implements Iterator<LocalDate> {

    LocalDate startDatum = null;

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public DatumsRechner(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    @Override
    public LocalDate next() {
        return this.startDatum = startDatum.plusDays(1);
    }

    public static LocalDate getStartDay() {
        LocalDate firstDayofLastWeek = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).minusDays(1);

        return firstDayofLastWeek;
    }


    @Override
    public boolean hasNext() {
        return true;
    }
}
