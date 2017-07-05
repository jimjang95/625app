package mil.army.a1div.jinu.a625app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileController {
	/*
	Will assume data looks like following.
	DATE(YYYY-MM-DD), RANK, NAME
	*/

	private String line;
	// private Soldier[] soldiers;
	private ArrayList<Soldier> soldiers;
	public int size = 0;

	public FileController(BufferedReader bufferedReader) {
		soldiers = new ArrayList<>();
		// soldiers = new Soldier[11359];
		int count = 0;
		try {
			// Read file and split line
			while((line = bufferedReader.readLine()) != null) {
				String field[] = line.split(",");
				String sosok = field[0];
				String name = field[1];
				Rank rank;
				if(field[2] == null || field[2].isEmpty()) { // isEmpty 라는 함수가 있어여!!
					rank = Rank.누락;
				} else {
					rank = Rank.valueOf(field[2]);
				}

				// parse date
				int y, m, d;

				if(field.length < 4) {
					y = 0;
					m = 0;
					d = 0;
				} else {
					// Parse to date
					String dateq = ""; // dateq 나중에 이름 date로 바꾸기
					if(field[3] == null || field[3].isEmpty()) {
						dateq = "누락"; // TODO: 이 부분 누락일 경우로 알아서 짜줘영
					}

					String year_s = field[3].substring(0, 4);
					String month_s = field[3].substring(4, 6);
					String day_s = field[3].substring(6);

					y = Integer.parseInt(year_s);
					m = Integer.parseInt(month_s);
					d = Integer.parseInt(day_s);
				}
				soldiers.add(new Soldier(sosok, name, rank, y, m, d));
				size = soldiers.size();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}


}