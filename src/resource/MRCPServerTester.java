package resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

class MRCPServerTester {
	private static boolean _debug;
	private Socket tcp_serverSocket_;
	private BufferedInputStream tcp_serverReader_;
	private BufferedWriter tcp_serverWriter_;
	private boolean tcp_receiverIsTerminated_;
	private Receiver tcp_receiver_;
	private Socket serverSocket_;
	private BufferedInputStream serverReader_;
	private BufferedWriter serverWriter_;
	private int seqenceId_;
	private boolean receiverIsTerminated_;
	private boolean receiverReceivedResult_;
	private Receiver receiver_;
	private String sessionId_;
	private int rtpLocalPort_;
	private String rtpHost_;
	private int rtpPort_;
	private Sender sender_;
	private Sender subSender_;
	private BufferedReader console_ = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws Exception {
		if (args.length > 1 && args[1].equals("debug")
		 || args.length > 2 && args[2].equals("debug")) {
			_debug = true;
		}
		MRCPServerTester tester = new MRCPServerTester(args[0], args.length > 1 && args[1].equals("loop"));
	}

	public MRCPServerTester(String scriptFileName, boolean loop) throws Exception {
		do {
			try {
				boolean buffering = false;
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(scriptFileName), "UTF-8"));
				ArrayList<String> lines = new ArrayList<String>();
				String line = reader.readLine();
				while (line != null) {
					if (line.startsWith("# ")) {
						line = reader.readLine();
						continue;
					}
					if (line.startsWith("----")) {
						if (buffering) {
							buffering = false;
						}
						String command = line.substring(4).trim();
						if (command.length() > 0 && command.charAt(0) != '-') {
							String[] items = command.split("\\s+");
							if (items.length > 0) {
								if (_debug) {
									System.out.println("---- " + command);
								}
								if (items[0].equals("udp_send")) {
									if (items.length > 1) {
										udp_send(items[1], lines);
									}
								} else
								if (items[0].equals("udp_receive")) {
									udp_receive();
								} else
								if (items[0].equals("tcp_open")) {
									if (items.length > 1) {
										tcp_open(items[1]);
									}
								} else
								if (items[0].equals("tcp_close")) {
									tcp_close();
								} else
								if (items[0].equals("tcp_send")) {
									tcp_send(lines, items.length > 1 && items[1].equals("-"));
								} else
								if (items[0].equals("open")) {
									if (items.length > 1) {
										open(items[1]);
									}
								} else
								if (items[0].equals("close")) {
									close();
								} else
								if (items[0].equals("send")) {
									send(lines, items.length > 1 && items[1].equals("-"));
								} else
								if (items[0].equals("play")) {
									if (items.length > 1) {
										play(items[1]);
									}
								} else
								if (items[0].equals("dtmf")) {
									if (items.length > 2) {
										dtmf(items[1], items[2]);
									}
								} else
								if (items[0].equals("record")) {
									record();
								} else
								if (items[0].equals("wait")) {
									if (items.length > 1) {
										wait(items[1]);
									}
								} else
								if (items[0].equals("sleep")) {
									if (items.length > 1) {
										sleep(items[1]);
									}
								} else
								if (items[0].equals("pause")) {
									pause();
								}
								if (_debug) {
									System.out.println("//// " + command);
								}
							}
						}
					} else {
						if (!buffering) {
							buffering = true;
							lines.clear();
						}
						lines.add(line);
						if (line.startsWith("m=audio ")) {
							int index = line.indexOf(' ', 8);
							if (index > 8) {
								rtpLocalPort_ = Integer.parseInt(line.substring(8, index));
							}
						}
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (loop);
		System.exit(0);
	}

	private DatagramSocket udpSocket_ = new DatagramSocket();
	private byte[] udpData_ = new byte[4096];
	private DatagramPacket udpPacket_ = new DatagramPacket(udpData_, 0, udpData_.length);
	private byte[] udpReceivingData_ = new byte[4096];
	private DatagramPacket udpReceivingPacket_ = new DatagramPacket(udpReceivingData_, 0, udpReceivingData_.length);

	private String mrcpv2_serverHost_ = null;
	private int mrcpv2_serverPort_ = 0;
	private boolean mrcpv2_serverType_ = false;
	private String mrcpv2_sessionId_ = null;

	private void udp_send(String serverName, ArrayList<String> lines) throws Exception {
		if (!serverName.equals("-")) {
			String serverHost;
			int serverPort;
			int colonIndex = serverName.indexOf(':');
			if (colonIndex != -1) {
				serverHost = serverName.substring(0, colonIndex);
				serverPort = Integer.parseInt(serverName.substring(colonIndex + 1));
			} else {
				serverHost = serverName;
				serverPort = 5060;
			}
			udpPacket_.setAddress(InetAddress.getByName(serverHost));
			udpPacket_.setPort(serverPort);
		}
		String string = completeRequest(lines);
		System.out.println("----[C->S]------------------------------------------------------------");
		System.out.print(string);
		System.out.println("----------------------------------------------------------------------");
		udpPacket_.setData(string.getBytes());
		udpSocket_.send(udpPacket_);
	}

	private void udp_receive() throws Exception {
		udpSocket_.receive(udpReceivingPacket_);
		String string = new String(udpReceivingData_, 0, udpReceivingPacket_.getLength());
		System.out.println("----[C<-S]------------------------------------------------------------");
		System.out.print(string);
		System.out.println("----------------------------------------------------------------------");
		{
			int index1 = string.indexOf("\nc=IN IP4 ");
			if (index1 != -1) {
				index1 += 10;
				int index2 = string.indexOf('\r', index1);
				if (index2 != -1) {
					mrcpv2_serverHost_ = rtpHost_ = string.substring(index1, index2);
				}
			}
		}
		{
			int index1 = string.indexOf("\nm=application ");
			if (index1 != -1) {
				index1 += 15;
				int index2 = string.indexOf(' ', index1);
				if (index2 != -1) {
					int index3 = index2 + 1;
					int index4 = string.indexOf(' ', index3);
					if (index4 != -1) {
						mrcpv2_serverPort_ = Integer.parseInt(string.substring(index1, index2));
						mrcpv2_serverType_ = string.substring(index3, index4).equals("TCP/TLS/MRCPv2");
					}
				}
			}
		}
		{
			int index1 = string.indexOf("\na=channel:");
			if (index1 != -1) {
				index1 += 11;
				int index2 = string.indexOf('@', index1);
				if (index2 != -1) {
					mrcpv2_sessionId_ = string.substring(index1, index2);
				}
			}
		}
		{
			int index1 = string.indexOf("\nm=audio ");
			if (index1 != -1) {
				index1 += 9;
				int index2 = string.indexOf(' ', index1);
				if (index2 != -1) {
					rtpPort_ = Integer.parseInt(string.substring(index1, index2));
				}
			}
		}
	}

	private void tcp_open(String serverName) throws Exception {
		// サーバ名の解析
		String serverHost;
		int serverPort;
		boolean serverType;
		int colonIndex = serverName.indexOf(':');
		if (colonIndex != -1) {
			serverHost = serverName.substring(0, colonIndex);
			serverPort = Integer.parseInt(serverName.substring(colonIndex + 1));
			serverType = false;
		} else {
			serverHost = serverName;
			serverPort = 554;
			serverType = false;
		}

		// サーバとの通信ソケットの作成
		boolean connected = false;
		int timeout = 10 * 1000; // とりあえず接続待ち時間を最大 10 秒に設定...
		while (true) {
			try {
				if (serverType) {
					tcp_serverSocket_ = createSSLSocket(serverHost, serverPort);
				} else {
					tcp_serverSocket_ = new Socket(InetAddress.getByName(serverHost), serverPort);
				}
				break;
			} catch (UnknownHostException e) {
				throw e;
			} catch (ConnectException e) {
				timeout -= 2 * 1000;
				if (timeout < 0) {
					throw e;
				}
				System.out.println("- INFO: retrying to connect to server: " + serverHost + ":" + serverPort + " ...");
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException ee) {}
			}
		}
		System.out.println("INFO: connected to server: " + serverHost + ":" + serverPort);

		// サーバとの入出力ストリームの取得
		tcp_serverReader_ = new BufferedInputStream(tcp_serverSocket_.getInputStream());
		tcp_serverWriter_ = new BufferedWriter(new OutputStreamWriter(tcp_serverSocket_.getOutputStream(), "UTF-8"));

		// 受信スレッドの作成
		tcp_receiverIsTerminated_ = false;
		tcp_receiver_ = new Receiver(this, tcp_serverReader_, "TCPReceiver");
		tcp_receiver_.start();
	}

	private void tcp_close() {
		try {
			// サーバとの通信ソケットのクローズ
			if (tcp_serverSocket_ != null) {
				tcp_serverSocket_.close();
				tcp_serverSocket_ = null;
			}

			// 受信スレッドの破棄
			tcp_receiver_.join();
			tcp_receiver_ = null;
			System.out.println("INFO: closed from server");
		} catch (Exception e) {}
	}

	private void tcp_send(ArrayList<String> lines, boolean nonBlocking) {
		if (tcp_receiverIsTerminated_) {
			return;
		}
		try {
			synchronized (this) {
				String string = completeRequest(lines);
				System.out.println("----[C->S]------------------------------------------------------------");
				System.out.print(string);
				if (nonBlocking) {
					System.out.println("----------------------------------------------------------------------");
				}
				tcp_serverWriter_.write(string);
				tcp_serverWriter_.flush();
				if (!nonBlocking) {
					if (!tcp_receiverIsTerminated_) {
						wait();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void open(String serverName) throws Exception {
		// サーバ名の解析
		String serverHost;
		int serverPort;
		boolean serverType;
		if (serverName.equals("*")) {
			serverHost = mrcpv2_serverHost_;
			serverPort = mrcpv2_serverPort_;
			serverType = mrcpv2_serverType_;
		} else {
			int colonIndex = serverName.indexOf(':');
			if (colonIndex != -1) {
				serverHost = serverName.substring(0, colonIndex);
				serverPort = Integer.parseInt(serverName.substring(colonIndex + 1));
				serverType = false;
			} else {
				serverHost = serverName;
				serverPort = 554;
				serverType = false;
			}
		}

		// サーバとの通信ソケットの作成
		boolean connected = false;
		int timeout = 10 * 1000; // とりあえず接続待ち時間を最大 10 秒に設定...
		while (true) {
			try {
				if (serverType) {
					serverSocket_ = createSSLSocket(serverHost, serverPort);
				} else {
					serverSocket_ = new Socket(InetAddress.getByName(serverHost), serverPort);
				}
				break;
			} catch (UnknownHostException e) {
				throw e;
			} catch (ConnectException e) {
				timeout -= 2 * 1000;
				if (timeout < 0) {
					throw e;
				}
				System.out.println("- INFO: retrying to connect to server: " + serverHost + ":" + serverPort + " ...");
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException ee) {}
			}
		}
		System.out.println("INFO: connected to server: " + serverHost + ":" + serverPort);

		// サーバとの入出力ストリームの取得
		serverReader_ = new BufferedInputStream(serverSocket_.getInputStream());
		serverWriter_ = new BufferedWriter(new OutputStreamWriter(serverSocket_.getOutputStream(), "UTF-8"));

		// シーケンス ID の初期化
		seqenceId_ = 100;

		// 受信スレッドの作成
		receiverIsTerminated_ = false;
		receiver_ = new Receiver(this, serverReader_, "Receiver");
		receiver_.start();
	}

	private void close() {
		try {
			// サーバとの通信ソケットのクローズ
			if (serverSocket_ != null) {
				serverSocket_.close();
				serverSocket_ = null;
			}

			// 受信スレッドの破棄
			receiver_.join();
			receiver_ = null;

			// 送信スレッドの破棄
			receiverIsTerminated_ = false;
			if (sender_ != null) {
				sender_.cancel();
				sender_ = null;
			}
			if (subSender_ != null) {
				subSender_.cancel();
				subSender_ = null;
			}
			System.out.println("INFO: closed from server");
		} catch (Exception e) {}
	}

	private void send(ArrayList<String> lines, boolean nonBlocking) {
		if (receiverIsTerminated_) {
			return;
		}
		receiverReceivedResult_ = false;
		try {
			synchronized (this) {
				String string = completeRequest(lines);
				System.out.println("----[C->S]------------------------------------------------------------");
				System.out.print(string);
				if (nonBlocking) {
					System.out.println("----------------------------------------------------------------------");
				}
				serverWriter_.write(string);
				serverWriter_.flush();
				if (!nonBlocking) {
					if (!receiverIsTerminated_) {
						wait();
					}
				}
				seqenceId_++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play(String audioFileName) throws Exception {
		if (receiverIsTerminated_) {
			return;
		}
		if (sender_ != null) {
			sender_.cancel();
			sender_ = null;
		}
		if (subSender_ != null) {
			subSender_.cancel();
			subSender_ = null;
		}
		System.out.println("INFO: sending audio file: " + audioFileName + " --> " + rtpHost_ + ":" + rtpPort_ + " ...");
		sender_ = new DataReader(rtpLocalPort_, rtpHost_, rtpPort_, audioFileName);
		sender_.start();
	}

	private void dtmf(String keys, String intervalTime) throws Exception {
		if (receiverIsTerminated_) {
			return;
		}
		if (sender_ != null) {
			sender_.cancel();
			sender_ = null;
		}
		if (subSender_ != null) {
			subSender_.cancel();
			subSender_ = null;
		}
		System.out.println("INFO: sending keys: " + keys + " (" + intervalTime + " [sec.]) --> " + rtpHost_ + ":" + rtpPort_ + " ...");
		sender_ = new EventReader(rtpLocalPort_, rtpHost_, rtpPort_, keys, Integer.parseInt(intervalTime));
		sender_.start();
	}

	private void record() throws Exception {
		if (receiverIsTerminated_) {
			return;
		}
		if (sender_ != null) {
			sender_.cancel();
			sender_ = null;
		}
		if (subSender_ != null) {
			subSender_.cancel();
			subSender_ = null;
		}
		System.out.println("INFO: recording audio data and keys --> " + rtpHost_ + ":" + rtpPort_ + " ...");
		sender_ = new DataRecorder(rtpLocalPort_, rtpHost_, rtpPort_, /* maxRecordingTime */ 0);
		sender_.start();
		subSender_ = new EventRecorder(rtpHost_, rtpPort_, /* maxRecordingTime */ 0);
		subSender_.start();
	}

	private void wait(String timeout) throws Exception {
		if (receiverIsTerminated_) {
			return;
		}
		System.out.println("INFO: waiting: " + timeout + " [sec.] ...");
		try {
			synchronized (this) {
				if (timeout.equals("-")) {
					while (!receiverReceivedResult_) {
						wait();
					}
				} else {
					wait(Integer.parseInt(timeout) * 1000);
				}
			}
		} catch (Exception e) {}
		if (sender_ != null) {
			sender_.cancel();
			sender_ = null;
		}
		if (subSender_ != null) {
			subSender_.cancel();
			subSender_ = null;
		}
		System.out.println("INFO: waited");
	}

	private void sleep(String timeout) {
		if (receiverIsTerminated_) {
			return;
		}
		System.out.println("INFO: sleeping: " + timeout + " [sec.] ...");
		try {
			Thread.sleep(Integer.parseInt(timeout) * 1000);
		} catch (Exception e) {}
		System.out.println("INFO: slept");
	}

	private void pause() {
		if (receiverIsTerminated_) {
			return;
		}
		System.out.println("INFO: pausing ...");
		try {
			console_.readLine();
		} catch (Exception e) {}
	}

	private String completeRequest(ArrayList<String> lines) throws Exception {
		String seqenceIdString = String.valueOf(seqenceId_);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.equalsIgnoreCase("CSeq: ###")) {
				lines.set(i, "CSeq: " + seqenceIdString);
			} else
			if (line.length() > 13 && line.substring(line.length() - 13).equalsIgnoreCase(" ### MRCP/1.0")) {
				lines.set(i, line.substring(0, line.length() - 12) + seqenceIdString + " MRCP/1.0");
			} else
			if (line.equalsIgnoreCase("Session: *")) {
				lines.set(i, "Session: " + sessionId_);
			} else
			if (line.startsWith("MRCP/2.0 ") && line.endsWith(" ###")) {
				lines.set(i, line.substring(0, line.length() - 3) + seqenceIdString);
			} else
			if (line.length() >= 21 && line.substring(0, 21).equalsIgnoreCase("channel-identifier: *")) {
				lines.set(i, line.substring(0, 20) + mrcpv2_sessionId_ + line.substring(21));
			} else
			if (line.length() >= 27 && line.substring(0, 27).equalsIgnoreCase("vendor-specific-parameters:")) {
				boolean necessaryURLEncoding = false;
				for (int j = 0; j < line.length(); j++) {
					char c = line.charAt(j);
					if (c >= 0x80) {
						necessaryURLEncoding = true;
						break;
					}
					if (c == '%') {
						if (j + 3 > line.length()) {
							necessaryURLEncoding = true;
							break;
						}
						char c1 = line.charAt(++j);
						char c2 = line.charAt(++j);
						if (!(c1 >= '0' && c1 <= '9' || c1 >= 'A' && c1 <= 'F' || c1 >= 'a' && c1 <= 'f')) {
							necessaryURLEncoding = true;
							break;
						}
						if (!(c2 >= '0' && c2 <= '9' || c2 >= 'A' && c2 <= 'F' || c2 >= 'a' && c2 <= 'f')) {
							necessaryURLEncoding = true;
							break;
						}
					}
				}
				if (necessaryURLEncoding) {
					StringBuilder buffer = new StringBuilder();
					for (int j = 0; j < line.length(); j++) {
						char c = line.charAt(j);
						if (c >= 0x800) {
							// EEEE DDCC CCBB AAAA → '%' bit(1110) bit(EEEE) '%' bit(10DD) bit(CCCC) '%' bit(10BB) bit(AAAA)
							int EEEE = (c >> 12) & 0xF;
							int DD = (c >> 10) & 0x3;
							int CCCC = (c >> 6) & 0xF;
							int BB = (c >> 4) & 0x3;
							int AAAA = c & 0xF;
							buffer.append("%E");
							if (EEEE < 10) {
								buffer.append((char)('0' + EEEE));
							} else {
								buffer.append((char)('A' + EEEE - 10));
							}
							buffer.append('%');
							if (DD + 8 < 10) {
								buffer.append((char)('0' + DD + 8));
							} else {
								buffer.append((char)('A' + (DD + 8) - 10));
							}
							if (CCCC < 10) {
								buffer.append((char)('0' + CCCC));
							} else {
								buffer.append((char)('A' + CCCC - 10));
							}
							buffer.append('%');
							if (BB + 8 < 10) {
								buffer.append((char)('0' + BB + 8));
							} else {
								buffer.append((char)('A' + (BB + 8) - 10));
							}
							if (AAAA < 10) {
								buffer.append((char)('0' + AAAA));
							} else {
								buffer.append((char)('A' + AAAA - 10));
							}
							continue;
						}
						if (c >= 0x80) {
							// 0000 0DCC CCBB AAAA → '%' bit(110D) bit(CCCC) '%' bit(10BB) bit(AAAA)
							int D = (c >> 10) & 0x1;
							int CCCC = (c >> 6) & 0xF;
							int BB = (c >> 4) & 0x3;
							int AAAA = c & 0xF;
							buffer.append('%');
							if (D + 8 < 10) {
								buffer.append((char)('0' + D + 8));
							} else {
								buffer.append((char)('A' + (D + 8) - 10));
							}
							if (CCCC < 10) {
								buffer.append((char)('0' + CCCC));
							} else {
								buffer.append((char)('A' + CCCC - 10));
							}
							buffer.append('%');
							if (BB + 8 < 10) {
								buffer.append((char)('0' + BB + 8));
							} else {
								buffer.append((char)('A' + (BB + 8) - 10));
							}
							if (AAAA < 10) {
								buffer.append((char)('0' + AAAA));
							} else {
								buffer.append((char)('A' + AAAA - 10));
							}
							continue;
						}
						if (c == '%') {
							buffer.append("%25");
							continue;
						}
						buffer.append(c);
					}
					lines.set(i, buffer.toString());
				}
			}
		}
		int lineIndex = lines.size();
		int contentLength = 0;
		int growingContentLength = 0;
		while (lineIndex > 0) {
			lineIndex--;
			String line = lines.get(lineIndex);
			if (line.length() == 0) {
				contentLength = growingContentLength;
			} else
			if (line.equalsIgnoreCase("Content-Length: *")) {
				line = "Content-Length: " + contentLength;
				lines.set(lineIndex, line);
			}
			growingContentLength += line.getBytes("UTF-8").length + 2;
		}
		StringBuilder buffer = new StringBuilder();
		for (String line: lines) {
			buffer.append(line);
			buffer.append("\r\n");
		}
		return buffer.toString();
	}

	private Socket createSSLSocket(String host, int port) throws IOException {
		javax.net.SocketFactory socketFactory = null;
		try {
			javax.net.ssl.TrustManager[] trustManagers = {
				new javax.net.ssl.X509TrustManager() {
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					}
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					}
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
			};
			javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagers, null);
			socketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new IOException("SSL initialization error", e);
		}
		return socketFactory.createSocket(host, port);
	}

	class Receiver extends Thread {
		private Object lock_;
		private BufferedInputStream inputStream_;
		private int contentLength_;
		private boolean state_;
		private byte[] header_;
		private int headerLength_;
		private byte[] body_;
		private int bodyLength_;

		public Receiver(Object lock, BufferedInputStream inputStream, String name) {
			lock_ = lock;
			inputStream_ = inputStream;
			contentLength_ = 0;
			state_ = false;
			header_ = new byte[1024];
			headerLength_ = 0;
			body_ = new byte[1024];
			bodyLength_ = 0;
			setName(name);
		}

		public void run() {
			try {
				byte[] line_ = new byte[1024];
				int lineLength_ = 0;
				int data = inputStream_.read();
				while (data != -1) {
					if (line_.length < lineLength_ + 1) {
						byte[] newLine = new byte[lineLength_ + 1024];
						System.arraycopy(line_, 0, newLine, 0, lineLength_);
						line_ = newLine;
					}
					line_[lineLength_++] = (byte)data;
					if (data == '\n') {
						break;
					}
					data = inputStream_.read();
				}
				while (lineLength_ > 0) {
					String line = new String(line_, 0, lineLength_, "UTF-8").trim();
					if (_debug) {
						System.out.println("<<<< " + line);
					}
					if (!state_) {
						if (header_.length < headerLength_ + lineLength_) {
							byte[] newHeader = new byte[headerLength_ + lineLength_ + 1024];
							System.arraycopy(header_, 0, newHeader, 0, headerLength_);
							header_ = newHeader;
						}
						System.arraycopy(line_, 0, header_, headerLength_, lineLength_);
						headerLength_ += lineLength_;
						if (line.startsWith("MRCP/2.0 ") && line.indexOf(" RECOGNITION-COMPLETE ") != -1) {
							receiverReceivedResult_ = true;
						} else
						if (line.startsWith("Session: ")) {
							sessionId_ = line.substring(9);
						} else
						if (line.startsWith("Content-Length: ")) {
							try {
								contentLength_ = Integer.parseInt(line.substring(16));
							} catch (Exception e) {}
						}
						if (line.length() == 0) {
							if (contentLength_ == 0) {
								synchronized (lock_) {
									System.out.println("----[C<-S]------------------------------------------------------------");
									System.out.print(new String(header_, 0, headerLength_, "UTF-8"));
									System.out.println("----------------------------------------------------------------------");
									lock_.notify();
								}
								contentLength_ = 0;
								state_ = false;
								headerLength_ = 0;
								bodyLength_ = 0;
							} else {
								state_ = true;
							}
						}
					} else {
						if (body_.length < bodyLength_ + lineLength_) {
							byte[] newHeader = new byte[bodyLength_ + lineLength_ + 1024];
							System.arraycopy(body_, 0, newHeader, 0, bodyLength_);
							body_ = newHeader;
						}
						System.arraycopy(line_, 0, body_, bodyLength_, lineLength_);
						bodyLength_ += lineLength_;
						if (line.startsWith("c=IN IP4 ")) {
							mrcpv2_serverHost_ = rtpHost_ = line.substring(9);
						} else
						if (line.startsWith("m=audio ")) {
							int index = line.indexOf(' ', 8);
							if (index != -1) {
								rtpPort_ = Integer.parseInt(line.substring(8, index));
							}
						} else
						if (line.startsWith("m=application ")) {
							int index1 = line.indexOf(' ', 14);
							if (index1 != -1) {
								int index2 = index1 + 1;
								int index3 = line.indexOf(' ', index2);
								if (index3 != -1) {
									mrcpv2_serverPort_ = Integer.parseInt(line.substring(14, index1));
									mrcpv2_serverType_ = line.substring(index2, index3).equals("TCP/TLS/MRCPv2");
								}
							}
						} else
						if (line.startsWith("a=channel:")) {
							int index = line.indexOf('@', 10);
							if (index != -1) {
								mrcpv2_sessionId_ = line.substring(10, index);
							}
						} else
						if (line.startsWith("RECOGNITION-COMPLETE ")) {
							receiverReceivedResult_ = true;
						}
						Phone phone = Phone.getPhone();
						if (phone != null) {
							// <input ...>～</input> が必ず 1 行で構成されていると仮定...
							int index1 = line.indexOf("<input ");
							if (index1 != -1) {
								int index2 = line.indexOf('>', index1 + 7);
								if (index2 != -1) {
									int index3 = line.indexOf("</input>", index2 + 1);
									String resultString = line.substring(index2 + 1, index3);
									if (resultString.equals("<noinput/>")) {
										resultString = "(no-input)";
									} else
									if (resultString.equals("<nomatch/>")) {
										resultString = "(no-match)";
									}
									phone.setResultString(resultString);
								}
							}
						}
						if (bodyLength_ == contentLength_) {
							synchronized (lock_) {
								System.out.println("----[C<-S]------------------------------------------------------------");
								System.out.print(new String(header_, 0, headerLength_, "UTF-8"));
								System.out.print(new String(body_, 0, bodyLength_, "UTF-8"));
								System.out.println("----------------------------------------------------------------------");
								if (startsWith(header_, "RTSP/1.0 ") || startsWith(header_, "SIP/2.0 ") || receiverReceivedResult_) {
									lock_.notify();
								}
							}
							contentLength_ = 0;
							state_ = false;
							headerLength_ = 0;
							bodyLength_ = 0;
						}
					}
					lineLength_ = 0;
					data = inputStream_.read();
					while (data != -1) {
						if (line_.length < lineLength_ + 1) {
							byte[] newLine = new byte[lineLength_ + 1024];
							System.arraycopy(line_, 0, newLine, 0, lineLength_);
							line_ = newLine;
						}
						line_[lineLength_++] = (byte)data;
						if (data == '\n') {
							break;
						}
						data = inputStream_.read();
					}
				}
			} catch (SocketException e) {
			} catch (Throwable t) {
				t.printStackTrace();
			}
			synchronized (lock_) {
				receiverIsTerminated_ = true;
				lock_.notify();
			}
		}

		boolean equals(byte[] data, String string) {
			int stringLength = string.length();
			if (data.length != stringLength) {
				return false;
			}
			for (int i = 0; i < stringLength; i++) {
				if ((int)data[i] != (int)string.charAt(i)) {
					return false;
				}
			}
			return true;
		}

		boolean startsWith(byte[] data, String string) {
			int stringLength = string.length();
			if (data.length < stringLength) {
				return false;
			}
			for (int i = 0; i < stringLength; i++) {
				if ((int)data[i] != (int)string.charAt(i)) {
					return false;
				}
			}
			return true;
		}
	}

// -----------------------------------------------------------------------
// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
// | | | | | | | | | | |1|1|1|1|1|1|1|1|1|1|2|2|2|2|2|2|2|2|2|2|3|3|
// |0|1|2|3|4|5|6|7|8|9|0|1|2|3|4|5|6|7|8|9|0|1|2|3|4|5|6|7|8|9|0|1|
// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
// | V |P|X|  CC   |M|     PT      |        sequence number        |
// +---+-+-+-------+-+-------------+-------------------------------+
// |                           timestamp                           |
// +---------------------------------------------------------------+
// |           synchronization source (SSRC) identifier            |
// +---------------------------------------------------------------+
// |            contributing source (CSRC) identifiers             |
// |                              ・                               |
// |                              ・                               |
// |                              ・                               |
// +---------------------------------------------------------------+
// |                          拡張ヘッダ                           |
// |                              ・                               |
// |                              ・                               |
// |                              ・                               |
// +---------------------------------------------------------------+
// ■ V (バージョン): 2ビット
//      RTP バージョン
//      現在は 2
// ■ P (パディング): 1ビット
//      ペイロードの最後にパディングオクテットがあるかどうか
// ■ X (拡張): 1ビット
//      RTP ヘッダの末尾に拡張ヘッダがあるかどうか
// ■ CC (CSRCカウント): 4ビット
//      CSRC フィールドの数
// ■ M (マーカ): 1ビット
//      音声の境界かどうか
//      ※ マーカの値はペイロードの種類によってそれぞれ規定される。
// ■ PT (ペイロードタイプ): 7ビット
//      ペイロードタイプ
// ■ sequence number (シーケンス番号): 16ビット
//      RTP パケット順序番号
//      初期値はランダムで以後 1 ずつ増加
// ■ timestamp (タイムスタンプ): 32ビット
//      RTP ペイロードの最初のデータのサンプリング時刻
//      初期値はランダムで以後一定値ずつ増加
// ■ SSRC (送信元ID): 32ビット
//      送信元を識別するための ID
// ■ CSRC (寄与ノードID): 32ビット
//      一つの RTP  パケットのペイロードに入っている全てのデータの送信元を
//      表す SSRC 群
//      ※ IP 電話では一つの RTP  パケットでは一つの送信元のデータしか送信
//         しないので、CSRC は使用されない。
// ■ 拡張ヘッダ
//      ペイロードタイプによって必要となる拡張ヘッダ
//
// ※ http://www.imodesu.info/osyaberi/rtpheader.html
// -----------------------------------------------------------------------

	protected abstract static class Sender extends Thread {
		// RTP データ形式
		protected static final byte ULAW = (byte)0x00;
		protected static final byte ALAW = (byte)0x08;
		protected static final byte MSB = (byte)0x0B;
		protected static final byte EVENT = (byte)0x65;

		// mu-Law 音声データ変換テーブル
		private static final short[] _ulaw2linear = {
			-32124, -31100, -30076, -29052, -28028, -27004, -25980, -24956,
			-23932, -22908, -21884, -20860, -19836, -18812, -17788, -16764,
			-15996, -15484, -14972, -14460, -13948, -13436, -12924, -12412,
			-11900, -11388, -10876, -10364,  -9852,  -9340,  -8828,  -8316,
			 -7932,  -7676,  -7420,  -7164,  -6908,  -6652,  -6396,  -6140,
			 -5884,  -5628,  -5372,  -5116,  -4860,  -4604,  -4348,  -4092,
			 -3900,  -3772,  -3644,  -3516,  -3388,  -3260,  -3132,  -3004,
			 -2876,  -2748,  -2620,  -2492,  -2364,  -2236,  -2108,  -1980,
			 -1884,  -1820,  -1756,  -1692,  -1628,  -1564,  -1500,  -1436,
			 -1372,  -1308,  -1244,  -1180,  -1116,  -1052,   -988,   -924,
			  -876,   -844,   -812,   -780,   -748,   -716,   -684,   -652,
			  -620,   -588,   -556,   -524,   -492,   -460,   -428,   -396,
			  -372,   -356,   -340,   -324,   -308,   -292,   -276,   -260,
			  -244,   -228,   -212,   -196,   -180,   -164,   -148,   -132,
			  -120,   -112,   -104,    -96,    -88,    -80,    -72,    -64,
			   -56,    -48,    -40,    -32,    -24,    -16,     -8,      0,
			 32124,  31100,  30076,  29052,  28028,  27004,  25980,  24956,
			 23932,  22908,  21884,  20860,  19836,  18812,  17788,  16764,
			 15996,  15484,  14972,  14460,  13948,  13436,  12924,  12412,
			 11900,  11388,  10876,  10364,   9852,   9340,   8828,   8316,
			  7932,   7676,   7420,   7164,   6908,   6652,   6396,   6140,
			  5884,   5628,   5372,   5116,   4860,   4604,   4348,   4092,
			  3900,   3772,   3644,   3516,   3388,   3260,   3132,   3004,
			  2876,   2748,   2620,   2492,   2364,   2236,   2108,   1980,
			  1884,   1820,   1756,   1692,   1628,   1564,   1500,   1436,
			  1372,   1308,   1244,   1180,   1116,   1052,    988,    924,
			   876,    844,    812,    780,    748,    716,    684,    652,
			   620,    588,    556,    524,    492,    460,    428,    396,
			   372,    356,    340,    324,    308,    292,    276,    260,
			   244,    228,    212,    196,    180,    164,    148,    132,
			   120,    112,    104,     96,     88,     80,     72,     64,
			    56,     48,     40,     32,     24,     16,      8,      0,
		};

		// A-Law 音声データ変換テーブル
		private static final short[] _alaw2linear = {
			 -5504,  -5248,  -6016,  -5760,  -4480,  -4224,  -4992,  -4736,
			 -7552,  -7296,  -8064,  -7808,  -6528,  -6272,  -7040,  -6784,
			 -2752,  -2624,  -3008,  -2880,  -2240,  -2112,  -2496,  -2368,
			 -3776,  -3648,  -4032,  -3904,  -3264,  -3136,  -3520,  -3392,
			-22016, -20992, -24064, -23040, -17920, -16896, -19968, -18944,
			-30208, -29184, -32256, -31232, -26112, -25088, -28160, -27136,
			-11008, -10496, -12032, -11520,  -8960,  -8448,  -9984,  -9472,
			-15104, -14592, -16128, -15616, -13056, -12544, -14080, -13568,
			  -344,   -328,   -376,   -360,   -280,   -264,   -312,   -296,
			  -472,   -456,   -504,   -488,   -408,   -392,   -440,   -424,
			   -88,    -72,   -120,   -104,    -24,     -8,    -56,    -40,
			  -216,   -200,   -248,   -232,   -152,   -136,   -184,   -168,
			 -1376,  -1312,  -1504,  -1440,  -1120,  -1056,  -1248,  -1184,
			 -1888,  -1824,  -2016,  -1952,  -1632,  -1568,  -1760,  -1696,
			  -688,   -656,   -752,   -720,   -560,   -528,   -624,   -592,
			  -944,   -912,  -1008,   -976,   -816,   -784,   -880,   -848,
			  5504,   5248,   6016,   5760,   4480,   4224,   4992,   4736,
			  7552,   7296,   8064,   7808,   6528,   6272,   7040,   6784,
			  2752,   2624,   3008,   2880,   2240,   2112,   2496,   2368,
			  3776,   3648,   4032,   3904,   3264,   3136,   3520,   3392,
			 22016,  20992,  24064,  23040,  17920,  16896,  19968,  18944,
			 30208,  29184,  32256,  31232,  26112,  25088,  28160,  27136,
			 11008,  10496,  12032,  11520,   8960,   8448,   9984,   9472,
			 15104,  14592,  16128,  15616,  13056,  12544,  14080,  13568,
			   344,    328,    376,    360,    280,    264,    312,    296,
			   472,    456,    504,    488,    408,    392,    440,    424,
			    88,     72,    120,    104,     24,      8,     56,     40,
			   216,    200,    248,    232,    152,    136,    184,    168,
			  1376,   1312,   1504,   1440,   1120,   1056,   1248,   1184,
			  1888,   1824,   2016,   1952,   1632,   1568,   1760,   1696,
			   688,    656,    752,    720,    560,    528,    624,    592,
			   944,    912,   1008,    976,    816,    784,    880,    848
		};

		// RTP シーケンス番号
		private static final java.util.concurrent.atomic.AtomicInteger _rtpSequenceId = new java.util.concurrent.atomic.AtomicInteger();

		// UDP 通信ソケット
		private DatagramSocket datagramSocket_;
		// RTP データ長
		private int rtpDataLength_;
		// RTP データ
		private byte[] rtpData_;
		// RTP パケット
		private DatagramPacket rtpPacket_;
		// キャンセルするかどうか
		protected boolean cancel_;
		// プレーヤ
		private Player player_;

		protected Sender(int localPort, String targetName, int targetPort, byte rtpDataType) throws Exception {
			// UDP 通信ソケットの作成
			if (localPort > 0) {
				datagramSocket_ = new DatagramSocket(localPort);
			} else {
				datagramSocket_ = new DatagramSocket();
			}

			// UDP 通信ソケットへのタイムアウトの設定
			datagramSocket_.setSoTimeout(20);

			// RTP データ長の決定
			if (rtpDataType == Sender.ULAW) {
				rtpDataLength_ = 160;
			} else
			if (rtpDataType == Sender.ALAW) {
				rtpDataLength_ = 160;
			} else
			if (rtpDataType == Sender.MSB) {
				rtpDataLength_ = 320;
			} else
			if (rtpDataType == Sender.EVENT) {
				rtpDataLength_ = 2;
			} else {
				rtpDataLength_ = 160;
			}

			// RTP データの確保
			rtpData_ = new byte[32768];

			// RTP データへの RTP データ形式の格納
			rtpData_[0] = (byte)0x80;
			rtpData_[1] = rtpDataType;

			// RTP パケットの作成
			rtpPacket_ = new DatagramPacket(rtpData_, 0, rtpData_.length, InetAddress.getByName(targetName), targetPort);

			// RTP データバイト数の RTP パケットへの格納
			rtpPacket_.setLength(12 + rtpDataLength_);

			// キャンセルフラグの初期化
			cancel_ = false;

			// プレーヤの初期化
			player_ = null;
		}

		protected byte[] getRtpData() {
			// RTP データの取得
			return rtpData_;
		}

		protected int getRtpDataOffset() {
			// RTP データオフセットの取得
			return 12;
		}

		protected int getRtpDataLength() {
			// RTP データ長の取得
			return rtpDataLength_;
		}

		protected void send() throws Exception {
			// RTP シーケンス番号のインクリメント
			int rtpSequenceId = _rtpSequenceId.incrementAndGet();

			// RTP データへの RTP シーケンス番号の格納
			rtpData_[2] = (byte)((rtpSequenceId >> 8) & 0xFF);
			rtpData_[3] = (byte)(rtpSequenceId & 0xFF);

			// UDP 通信ソケットによる RTP パケットの送信
			datagramSocket_.send(rtpPacket_);
		}

		public void receive() throws Exception {
			try {
				// UDP 通信ソケットによる RTP パケットの受信
				datagramSocket_.receive(rtpPacket_);

				// RTP パケットからの音声データの取り出し
				byte[] rtpData = rtpPacket_.getData();
				int rtpDataType = rtpData[1];
				int rtpDataLength = rtpPacket_.getLength() - 12;

				// 音声データ形式の PCM LSB への変換
				if (rtpDataType == Sender.ULAW) {
					int newRtpDataLength = rtpDataLength * 2;
					for (int i = 12 + rtpDataLength, j = 12 + newRtpDataLength; i > 12; i--, j -= 2) {
						short linear = _ulaw2linear[rtpData[i - 1] & 0xFF];
						rtpData[j - 2] = (byte)(linear & 0xFF);
						rtpData[j - 1] = (byte)(linear >> 8);
					}
					rtpDataLength = newRtpDataLength;
				} else
				if (rtpDataType == Sender.ALAW) {
					int newRtpDataLength = rtpDataLength * 2;
					for (int i = 12 + rtpDataLength, j = 12 + newRtpDataLength; i > 12; i--, j -= 2) {
						short linear = _alaw2linear[rtpData[i - 1] & 0xFF];
						rtpData[j - 2] = (byte)(linear & 0xFF);
						rtpData[j - 1] = (byte)(linear >> 8);
					}
					rtpDataLength = newRtpDataLength;
				} else
				if (rtpDataType == Sender.MSB) {
					for (int i = 12 + rtpDataLength; i > 12; i -= 2) {
						byte linear0 = rtpData[i - 2];
						byte linear1 = rtpData[i - 1];
						rtpData[i - 2] = linear1;
						rtpData[i - 1] = linear0;
					}
				}

				// プレーヤが作成されているかどうかのチェック
				if (player_ == null) {
					// プレーヤがまだ作成されていない場合...
					// プレーヤの作成
					player_ = new Player();
				}

				// プレーヤへの音声データの供給
				player_.play(rtpData, 12, rtpDataLength);
			} catch (InterruptedIOException e) {}
		}

		public void cancel() throws Exception {
			// キャンセルフラグの設定
			cancel_ = true;

			// 送信スレッドが終了するまで待機
			join();

			// UDP 通信ソケットのクローズ
			datagramSocket_.close();

			// プレーヤが作成されているかどうかのチェック
			if (player_ != null) {
				// プレーヤが作成されている場合...
				// プレーヤの停止
				player_.cancel();
			}
		}

		public abstract void run();
	}

	private class DataReader extends Sender {
		// 音声データファイル名
		private String audioFileName_;
		// ゼロ音声データ
		private byte rtpDataPadding_;

		public DataReader(int localPort, String targetName, int targetPort, String audioFileName) throws Exception {
			super(localPort, targetName, targetPort, (audioFileName.endsWith(".mu")) ? Sender.ULAW : (audioFileName.endsWith(".alaw")) ? Sender.ALAW : Sender.MSB);
			setName("DataReader");
			audioFileName_ = audioFileName;
			rtpDataPadding_ = (audioFileName.endsWith(".mu")) ? (byte)0xFF : (audioFileName.endsWith(".alaw")) ? (byte)0x55 : (byte)0x00;
		}

		public void run() {
			try {
				// RTP データ関連情報の取得
				byte[] rtpData = getRtpData();
				int rtpDataOffset = getRtpDataOffset();
				int rtpDataLength = getRtpDataLength();

				// ファイルのオープン
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(audioFileName_));

				// ファイルからの音声データの読み込み＆RTP データへの音声データの格納
				int currentRtpDataLength = in.read(rtpData, rtpDataOffset, rtpDataLength);
				while (!cancel_ && currentRtpDataLength > 0) {
					// 音声データの初期化
					for (int i = currentRtpDataLength; i < rtpDataLength; i++) {
						rtpData[rtpDataOffset + i] = rtpDataPadding_;
					}

					// 音声データを格納した RTP パケットの送信
					send();

					// 音声データを格納した RTP パケットの受信
					receive();

					// ファイルからの音声データの読み込み＆RTP データへの音声データの格納
					currentRtpDataLength = in.read(rtpData, rtpDataOffset, rtpDataLength);
				}

				// ファイルのクローズ
				in.close();

				// 音声データの初期化
				for (int i = 0; i < rtpDataLength; i++) {
					rtpData[rtpDataOffset + i] = rtpDataPadding_;
				}

				// 無音の音声データの送信
				while (!cancel_) {
					// 無音の音声データを格納した RTP パケットの送信
					send();

					// 音声データを格納した RTP パケットの受信
					receive();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class EventReader extends Sender {
		// キー文字列
		private String keys_;
		// キー送信間隔 (単位: 秒)
		private int interval_;
		// 電話
		private Phone phone_;

		public EventReader(int localPort, String targetName, int targetPort, String keys, int intervalTime) throws Exception {
			super(localPort, targetName, targetPort, Sender.EVENT);
			setName("EventReader");
			keys_ = keys;
			interval_ = intervalTime;
		}

		public void run() {
			try {
				// RTP データ関連情報の取得
				byte[] rtpData = getRtpData();
				int rtpDataOffset = getRtpDataOffset();
				int rtpDataLength = getRtpDataLength();

				// 指定されたキー文字列の解析
				for (int i = 0; !cancel_ && i < keys_.length(); i++) {
					// 指定されたキー文字列からのキー文字の取り出し
					char currentKey = keys_.charAt(i);

					// キー文字からのイベント ID の取り出し
					int eventId = -1;
					if (currentKey >= '0' && currentKey <= '9') {
						eventId = currentKey - '0';
					} else
					if (currentKey == '*') {
						eventId = 10;
					} else
					if (currentKey == '#') {
						eventId = 11;
					}

					// イベント ID の取り出しに成功したかどうかのチェック
					if (eventId != -1) {
						// イベント ID の取り出しに成功した場合...
						// RTP データへのイベント ID の格納
						rtpData[rtpDataOffset + 0] = (byte)eventId;
						rtpData[rtpDataOffset + 1] = /* endOfEvent */ (byte)0x80;

						// 指定されたキー送信間隔の待機
						Thread.sleep(interval_ * 1000);

						// イベント ID を格納した RTP パケットの送信
						send();
					} else {
						// イベント ID の取り出しに失敗した場合...
						// 1 秒間の待機
						Thread.sleep(1000);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class DataRecorder extends Sender {
		// 最大録音時間 (単位: 秒)
		private int maxRecordingTime_;
		// ゼロ音声データ
		private byte rtpDataPadding_;
		// 電話
		private Phone phone_;

		public DataRecorder(int localPort, String targetName, int targetPort, int maxRecordingTime) throws Exception {
			super(localPort, targetName, targetPort, Sender.MSB);
			setName("DataRecorder");
			maxRecordingTime_ = maxRecordingTime;
			rtpDataPadding_ = (byte)0x00;
			phone_ = Phone.createPhone();
			phone_.show();
		}

		public void run() {
			try {
				// RTP データ関連情報の取得
				byte[] rtpData = getRtpData();
				int rtpDataOffset = getRtpDataOffset();
				int rtpDataLength = getRtpDataLength();

				// 音声データ形式の取得
				javax.sound.sampled.AudioFormat audioFormat = new javax.sound.sampled.AudioFormat(/* samplesPerSec */ 8000, /* bitsPerSample */ 16, /* channels */ 1, /* signed */ true, /* bigEndian */ true);

				// 録音デバイスの取得
				javax.sound.sampled.TargetDataLine recordingDevice = (javax.sound.sampled.TargetDataLine)javax.sound.sampled.AudioSystem.getLine(new javax.sound.sampled.DataLine.Info(javax.sound.sampled.TargetDataLine.class, audioFormat));

				// 録音デバイスのオープン
				recordingDevice.open(audioFormat);

				// 録音の開始
				recordingDevice.start();

				// 録音時間の初期化
				int recordingTime = 0;

				// 録音開始時刻の計測
				long startTime = System.currentTimeMillis();

				// 録音デバイスからの音声データの録音＆RTP データへの音声データの格納
				int currentRtpDataLength = recordingDevice.read(rtpData, rtpDataOffset, rtpDataLength);
				while (!cancel_ && currentRtpDataLength > 0) {
					// 音声データの初期化
					for (int i = currentRtpDataLength; i < rtpDataLength; i++) {
						rtpData[rtpDataOffset + i] = rtpDataPadding_;
					}

					// 音声データを格納した RTP パケットの送信
					send();

					// 音声データを格納した RTP パケットの受信
					receive();

					// 音声データボリュームの計算
					int logPower = calculateLogPower(rtpData, rtpDataOffset, rtpDataLength);

					// 仮想電話への音声データボリュームの設定
					phone_.setMeterValue(logPower);

					// 録音時間の計測
					recordingTime = (int)(System.currentTimeMillis() - startTime);

					// 録音時間が最大録音時間を超過したかどうかのチェック
					if (maxRecordingTime_ != 0 && recordingTime >= maxRecordingTime_ * 1000) {
						// 録音時間が最大録音時間を超過した場合...
						break;
					}

					// 録音デバイスからの音声データの録音＆RTP データへの音声データの格納
					currentRtpDataLength = recordingDevice.read(rtpData, rtpDataOffset, rtpDataLength);
				}

				// 録音の停止
				recordingDevice.flush();

				// 録音デバイスのクローズ
				recordingDevice.close();
				System.out.println("INFO: ended recording audio data");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private int calculateLogPower(byte[] data, int offset, int length) {
			int sumLinear = 0;
			for (int i = 0; i < length; i += 2) {
				int pcm = ((int)data[offset + i] << 8) | ((int)data[offset + i + 1] & 0xFF);
				sumLinear += pcm;
			}
			int linear = sumLinear / 160;
			long sumPower = 0;
			for (int i = 0; i < length; i += 2) {
				int pcm = ((int)data[offset + i] << 8) | ((int)data[offset + i + 1] & 0xFF);
				sumPower += (pcm - linear) * (pcm - linear);
			}
			long power = sumPower / 160;
			int logPower = (power > 0.0) ? (int)(10.0 * Math.log10((double)power) - /* 経験的に... */ 30) : 0;
			if (logPower < 0) {
				logPower = 0;
			} else
			if (logPower > 50) {
				logPower = 50;
			}
			return logPower;
		}
	}

	class EventRecorder extends Sender {
		// 最大録音時間 (単位: 秒)
		private int maxRecordingTime_;
		// 仮想電話
		private Phone phone_;

		public EventRecorder(String targetName, int targetPort, int maxRecordingTime) throws Exception {
			super(/* localPort*/ 9, targetName, targetPort, Sender.EVENT);
			setName("EventRecorder");
			maxRecordingTime_ = maxRecordingTime;
			phone_ = Phone.createPhone();
			phone_.show();
		}

		public void run() {
			try {
				// RTP データ関連情報の取得
				byte[] rtpData = getRtpData();
				int rtpDataOffset = getRtpDataOffset();
				int rtpDataLength = getRtpDataLength();

				// 録音時間の初期化
				int recordingTime = 0;

				// 録音開始時刻の計測
				long startTime = System.currentTimeMillis();

				// 仮想電話からのキー文字の入力
				int currentKey = phone_.getCurrentKey();
				while (!cancel_ && currentKey != -1) {
					// キー文字からのイベント ID の取り出し
					int eventId = -1;
					if (currentKey >= '0' && currentKey <= '9') {
						eventId = currentKey - '0';
					} else
					if (currentKey == '*') {
						eventId = 10;
					} else
					if (currentKey == '#') {
						eventId = 11;
					}

					// イベント ID の取り出しに成功したかどうかのチェック
					if (eventId != -1) {
						// イベント ID の取り出しに成功した場合...
						// RTP データへのイベント ID の格納
						rtpData[rtpDataOffset + 0] = (byte)eventId;
						rtpData[rtpDataOffset + 1] = /* endOfEvent */ (byte)0x80;

						// イベント ID を格納した RTP パケットの送信
						send();
						System.out.println("INFO: sent event: " + eventId);
					} else {
						// イベント ID の取り出しに失敗した場合...
						// (何もしない)
					}

					// 録音時間の計測
					recordingTime = (int)(System.currentTimeMillis() - startTime);

					// 録音時間が最大録音時間を超過したかどうかのチェック
					if (maxRecordingTime_ != 0 && recordingTime >= maxRecordingTime_ * 1000) {
						// 録音時間が最大録音時間を超過した場合...
						break;
					}

					// 仮想電話からのキーの入力
					currentKey = phone_.getCurrentKey();
				}
				System.out.println("INFO: ended recording keys");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class Phone implements java.awt.event.ActionListener, java.awt.event.WindowListener, java.awt.event.KeyListener {
		// 仮想電話
		private static Phone phone_ = null;
		// キー文字
		private int currentKey_;
		// 表示サイズ
		private int width_;
		private int height_;
		// 表示位置
		private int x_;
		private int y_;
		// ボタン
		private javax.swing.JButton[] buttons_;
		// ラベル
		private javax.swing.JLabel label_;
		// メータ
		private Meter meter_;
		// 認識結果
		private javax.swing.JLabel result_;
		// フレーム
		private javax.swing.JFrame frame_;

		public static Phone createPhone() {
			if (phone_ == null) {
				phone_ = new Phone();
			}
			return phone_;
		}

		public static Phone getPhone() {
			return phone_;
		}

		public Phone() {
			currentKey_ = 0;
			width_ = 200;
			height_ = 300;
			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			x_ = (screenSize.width - width_) / 2;
			y_ = (screenSize.height - height_) / 2;
			frame_ = createFrame();
			frame_.setSize(width_, height_);
			frame_.setLocation(x_, y_);
		}

		public Phone(int width, int height) {
			width_ = width;
			height_ = height;
			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			x_ = (screenSize.width - width_) / 2;
			y_ = (screenSize.height - height_) / 2;
			frame_ = createFrame();
			frame_.setSize(width_, height_);
			frame_.setLocation(x_, y_);
		}

		public Phone(int width, int height, int x, int y) {
			width_ = width;
			height_ = height;
			x_ = x;
			y_ = y;
			frame_ = createFrame();
			frame_.setSize(width_, height_);
			frame_.setLocation(x_, y_);
		}

		public void show() {
			frame_.setVisible(true);
		}

		public void hide() {
			frame_.setVisible(false);
		}

		public void dispose() {
			frame_.dispose();
		}

		private javax.swing.JFrame createFrame() {
			java.awt.Font defaultFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 24);
			java.awt.Insets defaultMargin = new java.awt.Insets(2, 2, 2, 2);
			javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridBagLayout());
			panel.setBackground(java.awt.Color.WHITE);
			buttons_ = new javax.swing.JButton[12];
			java.awt.Dimension buttonSize = new java.awt.Dimension(20, 30);
			buttons_[ 1] = addButton(panel, 0, 0, "1", buttonSize, defaultFont, defaultMargin);
			buttons_[ 2] = addButton(panel, 1, 0, "2", buttonSize, defaultFont, defaultMargin);
			buttons_[ 3] = addButton(panel, 2, 0, "3", buttonSize, defaultFont, defaultMargin);
			buttons_[ 4] = addButton(panel, 0, 1, "4", buttonSize, defaultFont, defaultMargin);
			buttons_[ 5] = addButton(panel, 1, 1, "5", buttonSize, defaultFont, defaultMargin);
			buttons_[ 6] = addButton(panel, 2, 1, "6", buttonSize, defaultFont, defaultMargin);
			buttons_[ 7] = addButton(panel, 0, 2, "7", buttonSize, defaultFont, defaultMargin);
			buttons_[ 8] = addButton(panel, 1, 2, "8", buttonSize, defaultFont, defaultMargin);
			buttons_[ 9] = addButton(panel, 2, 2, "9", buttonSize, defaultFont, defaultMargin);
			buttons_[10] = addButton(panel, 0, 3, "*", buttonSize, defaultFont, defaultMargin);
			buttons_[ 0] = addButton(panel, 1, 3, "0", buttonSize, defaultFont, defaultMargin);
			buttons_[11] = addButton(panel, 2, 3, "#", buttonSize, defaultFont, defaultMargin);
			java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = java.awt.GridBagConstraints.RELATIVE;
			constraints.gridwidth = 3;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 0.2;
			constraints.fill = java.awt.GridBagConstraints.BOTH;
			label_ = new javax.swing.JLabel("----------", javax.swing.JLabel.CENTER);
			label_.setMinimumSize(buttonSize);
			label_.setPreferredSize(buttonSize);
			label_.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 30));
			panel.add(label_, constraints);
			meter_ = new Meter();
			java.awt.Dimension meterSize = new java.awt.Dimension(0, 20);
			meter_.setMinimumSize(meterSize);
			meter_.setPreferredSize(meterSize);
			meter_.setBorder(new javax.swing.border.EmptyBorder(2, 2, 2, 2));
			panel.add(meter_, constraints);
			result_ = new javax.swing.JLabel("", javax.swing.JLabel.CENTER);
			result_.setMinimumSize(buttonSize);
			result_.setPreferredSize(buttonSize);
			result_.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 30));
			panel.add(result_, constraints);
			javax.swing.JFrame frame = new javax.swing.JFrame("");
			frame.addWindowListener(this);
			frame.getContentPane().add(panel);
			return frame;
		}

		private javax.swing.JButton addButton(javax.swing.JPanel panel, int x, int y, String label, java.awt.Dimension size, java.awt.Font font, java.awt.Insets margin) {
			javax.swing.JButton button = new javax.swing.JButton(label);
			button.addActionListener(this);
			button.addKeyListener(this);
			button.setMinimumSize(size);
			button.setPreferredSize(size);
			button.setFont(font);
			button.setMargin(margin);
			java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
			constraints.gridx = x;
			constraints.gridy = y;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = java.awt.GridBagConstraints.BOTH;
			constraints.insets = margin;
			panel.add(button, constraints);
			return button;
		}

		public void setMeterValue(int meterValue) {
			meter_.setValue(meterValue);
		}

		public void setResultString(String resultString) {
			result_.setText(resultString);
		}

		public int getCurrentKey() {
			synchronized (this) {
				try {
					if (currentKey_ != -1) {
						wait(20);
					}
					int currentKey = currentKey_;
					currentKey_ = 0;
					return currentKey;
				} catch (InterruptedException e) {
					return -1;
				}
			}
		}

		public void actionPerformed(java.awt.event.ActionEvent ae) {
			synchronized (this) {
				currentKey_ = ((javax.swing.JButton)ae.getSource()).getText().charAt(0);
				StringBuilder text = new StringBuilder();
				text.append(label_.getText());
				text.append((char)currentKey_);
				if (text.length() > 10) {
					text.delete(0, text.length() - 10);
				}
				label_.setText(text.toString());
				notifyAll();
			}
		}

		public void windowActivated(java.awt.event.WindowEvent we) {
		}

		public void windowClosed(java.awt.event.WindowEvent we) {
			synchronized (this) {
				currentKey_ = -1;
				notifyAll();
			}
		}

		public void windowClosing(java.awt.event.WindowEvent we) {
		}

		public void windowDeactivated(java.awt.event.WindowEvent we) {
		}

		public void windowDeiconified(java.awt.event.WindowEvent we) {
		}

		public void windowIconified(java.awt.event.WindowEvent we) {
		}

		public void windowOpened(java.awt.event.WindowEvent we) {
		}

		public void keyPressed(java.awt.event.KeyEvent ke) {
			int c = ke.getKeyChar();
			int key = ke.getKeyChar() - '0';
			if (key == '*' - '0') {
				key = 10;
			} else
			if (key == '#' - '0') {
				key = 11;
			}
			if (key >= 0 && key < 12) {
				buttons_[key].requestFocusInWindow();
				buttons_[key].doClick();
			}
		}

		public void keyReleased(java.awt.event.KeyEvent ke) {
		}

		public void keyTyped(java.awt.event.KeyEvent ke) {
		}
	}

	static class Meter extends javax.swing.JComponent {
		public static final int HORIZONTAL = javax.swing.JProgressBar.HORIZONTAL;
		public static final int VERTICAL = javax.swing.JProgressBar.VERTICAL;

		private int value_ = 0;
		private int minValue_ = 0;
		private int maxValue_ = 50;
		private java.awt.Color lowColor_ = java.awt.Color.GREEN;
		private java.awt.Color midColor_ = java.awt.Color.YELLOW;
		private java.awt.Color highColor_ = java.awt.Color.RED;
		private java.awt.Color backgroundColor_ = java.awt.Color.BLACK;
		private int cellLength_ = 4;
		private int cellSpacing_ = 2;
		private int orientation_ = Meter.HORIZONTAL;

		public Meter() {
		}

		public void setValue(int value) {
			value_ = value;
			repaint();
		}

		public void paint(java.awt.Graphics g) {
			int x = 0;
			int y = 0;
			int width = getWidth();
			int height = getHeight();
			g.setColor(backgroundColor_);
			g.fillRect(x, y, width, height);
			java.awt.Insets margin = getInsets(); // area for border
			x = margin.left;
			y = margin.top;
			width -= margin.right + margin.left;
			height -= margin.bottom + margin.top;
			if (orientation_ == Meter.HORIZONTAL) {
				int low = width / 2;
				int mid = width * 3 / 4;
				// amount of progress to draw
				int current = (int)((double)width * (double)(value_ - minValue_) / (double)(maxValue_ - minValue_) + 0.5);
				// a cell and its spacing
				int increment = cellLength_ + cellSpacing_;
				if (current > 0) {
					if (cellSpacing_ == 0) {
						// draw one big Rect because there is no space between cells
						g.setColor(lowColor_);
						int offset = (current <= low) ? current : low;
						g.fillRect(x, y, offset, height);
						if (current > low) {
							g.setColor(midColor_);
							offset = (current <= mid) ? current : mid;
							offset -= low;
							g.fillRect(x + low, y, offset, height);
							if (current > mid) {
								g.setColor(highColor_);
								offset = current - mid;
								g.fillRect(x + mid, y, offset, height);
							}
						}
					} else {
						// draw each individual cells
						// the largest number to draw a cell at
						int max = x + current;
						for (int offset = x; offset < max; offset += increment) {
							if (offset >= mid) {
								g.setColor(highColor_);
							} else
							if (offset >= low) {
								g.setColor(midColor_);
							} else {
								g.setColor(lowColor_);
							}
							g.fillRect(offset, y, cellLength_, height);
						}
					}
				}
			} else {
				int low = height / 2;
				int mid = height * 3 / 4;
				// amount of progress to draw
				int current = (int)((double)height * (double)(value_ - minValue_) / (double)(maxValue_ - minValue_) + 0.5);
				// a cell and its spacing
				int increment = cellLength_ + cellSpacing_;
				if (current > 0) {
					if (cellSpacing_ == 0) {
						// draw one big Rect because there is no space between cells
						g.setColor(lowColor_);
						int offset = (current <= low) ? current : low;
						g.fillRect(x, y + height - offset, width, offset);
						if (current > low) {
							g.setColor(midColor_);
							offset = (current <= mid) ? current : mid;
							offset -= low;
							g.fillRect(x, y + height - offset - low, width, offset);
							if (current > mid) {
								g.setColor(highColor_);
								offset = current - mid;
								g.fillRect(x, y + height - offset - mid, width, offset);
							}
						}
					} else {
						// draw each individual cells
						// the smallest number to draw a cell at
						// that is, the number at the top
						int min = height - current;
						for (int offset = height - cellLength_ + cellSpacing_; offset >= min; offset -= increment) {
							if (offset < height - mid) {
								g.setColor(highColor_);
							} else
							if (offset < height - low) {
								g.setColor(midColor_);
							} else {
								g.setColor(lowColor_);
							}
							g.fillRect(x, offset, width, cellLength_);
						}
					}
				}
			}
		}
	}

	static class Player extends Thread {
		private byte[] data_;
		private int dataOffset_;
		private boolean cancel_;

		public Player() throws Exception {
			data_ = new byte[16000];
			dataOffset_ = 0;
			start();
/*
			byte[] data = new byte[320];
			for (int i = 0; i < 10; i++) {
				DataInputStream in = null;
				try {
					in = new DataInputStream(new FileInputStream("R:/MRCPServer.processing.a08"));
					int dataByteCount = in.read(data, 0, 320);
					while (dataByteCount > 0) {
						play(data, 0, dataByteCount);
						dataByteCount = in.read(data, 0, 320);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
							in = null;
						} catch (IOException e) {}
					}
				}
			}
*/
		}

		public void play(byte[] data, int dataOffset, int dataByteCount) throws Exception {
			synchronized (this) {
				while (!cancel_ && dataOffset_ + dataByteCount > data_.length) {
					wait(20);
				}
				if (cancel_) {
					return;
				}
				System.arraycopy(data, dataOffset, data_, dataOffset_, dataByteCount);
				dataOffset_ += dataByteCount;
				notify();
			}
		}

		public void cancel() throws Exception {
			synchronized (this) {
				cancel_ = true;
				notifyAll();
			}
			join();
		}

		public void run() {
			try {
				AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
				SourceDataLine dataLine = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, audioFormat));
				dataLine.open(audioFormat);
				dataLine.start();
				int dataOffset = 0;
				int newDataOffset = 0;
				while (true) {
					if (newDataOffset > dataOffset) {
						dataLine.write(data_, dataOffset, newDataOffset - dataOffset);
					}
					synchronized (this) {
						if (newDataOffset > 0) {
							if (newDataOffset < dataOffset_) {
								System.arraycopy(data_, newDataOffset, data_, 0, dataOffset_ - newDataOffset);
								dataOffset_ -= newDataOffset;
								dataOffset = 0;
							} else {
								dataOffset_ = 0;
								dataOffset = 0;
							}
						}
						newDataOffset = dataOffset_;
						while (!cancel_ && newDataOffset <= dataOffset) {
							wait();
							newDataOffset = dataOffset_;
						}
						if (cancel_) {
							break;
						}
					}
				}
				dataLine.drain();
				dataLine.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
