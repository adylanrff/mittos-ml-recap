import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Random;

public class RAID0InputGenerator {

	public static int entryCnt = 100000;
	public static int pendingCnt = 4;
	// public static int channelCnt = 8;
	public static int range = 65536;
	static int address_digit_max = 32;
	public static int unit = 40;
	public static int threshold = 100;
	public static int[] channelCnt = {8};
	public static String[] setHeaders = { "train","test" };

	public static void main(String[] argv) throws IOException {
		Random random = new Random();
		for (String setHeader : setHeaders) {
			for (int channel : channelCnt) {
				
				//
				String filePath = "../../data/raw/RAID0-" + setHeader + "-" + channel+".csv";
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
				//
				for (int i = 0; i < entryCnt; i++) {
					int target_address = random.nextInt(range);
					int target_actual_channel = target_address % channel;
					//Integer target_actual_channel = (target_address%channel+target_address/channel)%channel;
					int actual_lat = unit;
					String line = "";
					//
					String target_address_str = Integer.toBinaryString(target_address);
					for(int addr_digit_cnt=0;addr_digit_cnt<address_digit_max;addr_digit_cnt++){
						int index = addr_digit_cnt - (address_digit_max-target_address_str.length());
						if(index < 0){
							line += "0,";
						}else{
							line += target_address_str.charAt(index)+",";
						}
					}
					//
					for (int j = 0; j < pendingCnt; j++) {
						int pending_address = random.nextInt(range);
						int actual_pending_channel = pending_address % channel;
						//Integer actual_pending_channel = (pending_address%channel+pending_address/channel)%channel;
						String pending_address_str = Integer.toBinaryString(pending_address);
						for(int addr_digit_cnt=0;addr_digit_cnt<address_digit_max;addr_digit_cnt++){
							int index = addr_digit_cnt - (address_digit_max-pending_address_str.length());
							if(index < 0){
								line += "0,";
							}else{
								line += pending_address_str.charAt(index)+",";
							}
						}
						if (actual_pending_channel == target_actual_channel) {
							actual_lat += unit;
						}

					}
					
					int actual_ans = actual_lat >= threshold ? 1 : 0;
					line += actual_ans;
					writer.write(line + "\n");
				}
				//
				writer.close();
			}

		}
	}

}
