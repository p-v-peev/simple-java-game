package application;

public class Engine extends Thread {
	
	Main window = null;
	int liveShow = 0;
	int slowDownShow = 0;
	int liveInterval = 30;
	int slowDownInterval = 30;
	
	public Engine(Main window) {
		this.window = window;
	}

	public void run(){
		
		liveShow = generateRandom(window.points, window.points + liveInterval);
		do {
			slowDownShow = generateRandom(window.points, window.points + slowDownInterval);
		} while (liveShow == slowDownShow);
		
		while (true) {
			try {
				if (window.points == liveShow) {
					window.liveView.setTranslateX(generateRandom(32, (int)window.playField.getWidth() - 32));
					window.liveView.setTranslateY(generateRandom(32, (int)window.playField.getHeight() - 32));
					window.liveView.setVisible(true);
					liveShow = generateRandom(window.points, window.points + liveInterval);
					sleep(window.time);
					window.liveView.setVisible(false);
				}else if (window.points == slowDownShow) {
					window.slowDownView.setTranslateX(generateRandom(32, (int)window.playField.getWidth() - 32));
					window.slowDownView.setTranslateY(generateRandom(32, (int)window.playField.getHeight() - 32));
					window.slowDownView.setVisible(true);
					slowDownShow = generateRandom(window.points, window.points + slowDownInterval);
					sleep(window.time);
					window.slowDownView.setVisible(false);
				}else {
					window.spotView.setTranslateX(generateRandom(32, (int)window.playField.getWidth() - 32));
					window.spotView.setTranslateY(generateRandom(32, (int)window.playField.getHeight() - 32));
					window.spotView.setVisible(true);
					sleep(window.time);
					window.spotView.setVisible(false);
				}
				/*console
				System.out.println("Показване на живот на " + liveShow + "точки");
				System.out.println("Показване на забавяне на " + slowDownShow + "точки");
				System.out.println("Точки " + window.points);
				System.out.println("Интервал" + window.time);
				*/
				if (!window.clicked) {
					window.lives.set(window.lives.get() - 1);
					window.livesText.setText(String.valueOf((int)window.lives.get()));
				}
				window.clicked = false;
				sleep(generateRandom(500, 4000));
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	private int generateRandom(int min, int max) {
		int range = (max - min) + 1;     
		return (int)(Math.random() * range) + min;
	}
}
