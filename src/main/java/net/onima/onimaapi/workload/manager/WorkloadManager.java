package net.onima.onimaapi.workload.manager;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.workload.type.RankWorkload;
import net.onima.onimaapi.workload.type.SchedulerWorkload;

public class WorkloadManager {
	
	private WorkloadDistributor distributor;
	
	private int schedulerId, ranksId;
	
	public WorkloadManager(WorkloadDistributor distributor) {
		this.distributor = distributor;
	}
	
	public void registerWorkloads() {
		WorkloadThread scheduler = distributor.newThread(5);
		WorkloadThread ranks = distributor.newThread(5);
		
		schedulerId = scheduler.id;
		ranksId = ranks.id;
		
		OnimaAPI.getScheduled().forEach(scheduled -> scheduler.addWorkload(new SchedulerWorkload(scheduled)));
		APIPlayer.getOnlineAPIPlayers().forEach(player -> ranks.addWorkload(new RankWorkload(player)));
		
		distributor.startPerSecond(scheduler.id, true, 1L);
		distributor.startPerSecond(ranks.id, true, 1L);
	}
	
	public int getSchedulerId() {
		return schedulerId;
	}
	
	public int getRanksId() {
		return ranksId;
	}
	
}
