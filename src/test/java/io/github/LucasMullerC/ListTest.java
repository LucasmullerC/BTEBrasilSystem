package io.github.LucasMullerC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.pending.PendingService;

public class ListTest {
    private ServerMock server;
    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(BTEBrasilSystem.class);
    }

    @Test
    public void AddPendingListTest(){
        Pending pending = addNew("123","2");
        Pending SecondPending = addNew("124","5");

        PendingService pendingService = new PendingService();
        pendingService.addPending(pending);
        pendingService.addPending(SecondPending);

        Pending newAdded = pendingService.getPendingApplication("123");
        assertEquals(pending, newAdded);
    }

    @Test
    public void RemovePendingListTest(){
        Pending pending = addNew("123","2");
        Pending SecondPending = addNew("124","5");

        PendingService pendingService = new PendingService();
        pendingService.addPending(pending);
        pendingService.addPending(SecondPending);

        Pending newAdded = pendingService.getPendingApplication("124");
        pendingService.removePending(newAdded);
        assertNull(pendingService.getPendingApplication("124"));
    }

    @Test
    public void UpdatePendingListTest(){
        Pending pending = addNew("123","2");
        Pending SecondPending = addNew("124","5");

        PendingService pendingService = new PendingService();
        pendingService.addPending(pending);
        pendingService.addPending(SecondPending);

        Pending newAdded = pendingService.getPendingApplication("124");
        newAdded.setbuilds("150");
        pendingService.updatePending(newAdded);

        Pending updated = pendingService.getPendingApplication("124");
        assertEquals(updated.getbuilds(), "150");
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    public Pending addNew(String uuid, String builds){
        Pending pending = new Pending(uuid);
        pending.setbuilds(builds);
        pending.setisApplication(true);
        pending.setregionId("nulo");

        return pending;
    }
}
