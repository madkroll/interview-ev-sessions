package com.madkroll.inteview.evsessions.service;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * This class demonstrates concept of using Skip-List based collection to store sessions.
 * */
public class SkipListDemo {

    private final Comparator<Record> recordComparator =
            Comparator.comparing(Record::getDateTime)
                    .thenComparing(Record::getId);


    private final ConcurrentSkipListSet<Record> records = new ConcurrentSkipListSet<>(recordComparator);

    @Getter
    @ToString(of = {"updated", "finished"})
    public class Record {

        private final LocalDateTime dateTime;
        private final Set<UUID> ids = new HashSet<>();
        private int updated;
        private int finished;

        public Record(final UUID id, final LocalDateTime dateTime, final int updated, final int finished) {
            this.ids.add(id);
            this.dateTime = dateTime;
            this.updated = updated;
            this.finished = finished;
        }

        public Record(final LocalDateTime dateTime) {
            this.dateTime = dateTime;
            this.updated = 0;
            this.finished = 0;
        }

        public UUID getId() {
            return ids.stream().findFirst().orElse(null);
        }
    }

    /**
     * Inserts new record for update event:
     * O(log n)
     */
    public void update(final UUID id, final LocalDateTime dateTime) {
        records.add(new Record(id, dateTime, 1, 0));
    }

    /**
     * Insert new record for finish event:
     * O(log n)
     */
    public void finish(final UUID id, final LocalDateTime dateTime) {
        records.add(new Record(id, dateTime, 0, 1));
    }

    /**
     * This method should also remove update/finish duplicates.
     * However this can be done in one iteration with a help of set.
     *
     * So result time complexity is:
     * O(n)
     */
    public Set<Record> list() {
        // .. here should be removing duplicates code ..
        return ImmutableSet.copyOf(records);
    }

    /**
     * Tail-last-minute operation supposes to have O(log n) time complexity.
     *
     * Second step (reducing and counting) merges all records into one summary record.
     * To eliminate update and finish events for the same session id - ids are stored in hash set.
     * Even though such reducing is not so nice - selection of data it works with is not comparable to the whole data set.
     *
     * So I believe, in case such service works for at least one day - one minute data sub-set has a minor influence
     * on time complexity definition.
     * */
    public Record summaryRecord() {
        final ImmutableSet<Record> lastRecords =
                ImmutableSet.copyOf(this.records.tailSet(new Record(LocalDateTime.now().minusMinutes(1))));

        System.out.println(lastRecords.size());

        return lastRecords
                .stream()
                .reduce((record, record2) -> {
                    record.updated += record2.updated - Sets.intersection(record.ids, record2.ids).size();
                    record.finished += record2.finished;
                    record.ids.addAll(record2.ids);
                    return record;
                }).orElse(new Record(LocalDateTime.now()));
    }

    public static void main(String[] args) {
        final SkipListDemo context = new SkipListDemo();
        new Random()
                .ints(50, 0, 100)
                .forEach(operand -> context.update(UUID.randomUUID(), LocalDateTime.now().minusMinutes(operand)));

        context.records
                .stream()
                .limit(10)
                .forEach(record -> context.finish(record.getId(), LocalDateTime.now()));


        new Random()
                .ints(50, 0, 100)
                .forEach(operand -> context.update(UUID.randomUUID(), LocalDateTime.now().minusMinutes(operand)));

        System.out.println(context.summaryRecord());
    }
}