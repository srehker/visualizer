package org.powertac.visualizer.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.powertac.visualizer.domain.broker.BrokerModel;
import org.powertac.visualizer.interfaces.Recyclable;
import org.powertac.visualizer.interfaces.TimeslotCompleteActivation;
import org.powertac.visualizer.json.BrokersJSON;
import org.primefaces.json.JSONArray;
import org.springframework.stereotype.Service;

@Service
public class BrokerService implements TimeslotCompleteActivation, Recyclable,Serializable {

	private Logger log = Logger.getLogger(BrokerService.class);
	private static final long serialVersionUID = 1L;
	private Map<String, BrokerModel> map;
	private ArrayList<BrokerModel> brokers = new ArrayList<BrokerModel>();
	private BrokersJSON json = new BrokersJSON();

	public void addBrokers(HashMap<String, BrokerModel> brokers) {
		this.map = brokers;
		// build brokers list:
		this.brokers = new ArrayList<BrokerModel>(map.values());
	}

	/**
	 * @param name
	 * @return Broker model associated with a specified name, or null if the
	 *         broker cannot be found.
	 */
	public BrokerModel findBrokerByName(String name) {
		return map.get(name);

	}

	public void recycle() {
		map = null;
		brokers = new ArrayList<BrokerModel>();
		json = new BrokersJSON();
	}

	public BrokersJSON getJson() {
		return json;
	}

	public void setJson(BrokersJSON json) {
		this.json = json;
	}

	public void activate(int timeslotIndex) {
		// cash lineChart:
		JSONArray cashChartData = new JSONArray();
		// subscription pieChart:
		JSONArray customerCountData = new JSONArray();

		for (Iterator iterator = brokers.iterator(); iterator.hasNext();) {
			BrokerModel broker = (BrokerModel) iterator.next();
			
			log.trace("Updating broker model:"+broker.getName());
			// update broker's model
			broker.update(timeslotIndex);
			// collect data for global charts:
			cashChartData.put(broker.getJson().getCashBalanceJson());
			customerCountData.put(broker.getCustomerCount());
		}
		// update global charts:
		json.setCashChartData(cashChartData);
		json.setCustomerCountData(customerCountData);

	}

	public List<BrokerModel> getBrokerList() {
		return brokers;
	}

	public ArrayList<BrokerModel> getBrokers() {
		return new ArrayList<BrokerModel>(brokers);
	}

}