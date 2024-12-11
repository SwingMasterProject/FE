//import javax.management.timer.Timer;
import javax.swing.*;
import java.awt.event.*;
import java.net.http.HttpResponse;
import java.awt.*;
import java.util.*;
import java.util.List;
import com.google.gson.*;// json 데이터 저장방식 변경 ########################################
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Timer;


public class FE_KIOSK extends JFrame {
    //전역 변수 사용
    private int table_num = 1;
    private Map<String, List<Menu>> menuData = new LinkedHashMap<>();
    private Map<String, List<Order_list>> orderList = new LinkedHashMap<>();
    private Map<String, List<Order_list>> orderList_final = new LinkedHashMap<>();
    private Map<String, Integer[]> check = new LinkedHashMap<>();
    private Map<String, String[]> requestList = new LinkedHashMap<>();
    private Container c;
    private Container left_c;
    private Container mid_c;
    private Container right_c;
    private JPanel leftly_main;
    private JPanel leftly;
    private JPanel leftly_down;
    private JPanel midly_main;
    private JPanel midly;
    private JPanel midly_down;
    private JScrollPane midly_menu;
    private JPanel rightly_main;
    private JPanel rightly;
    private JPanel rightly_down;
    String store_name = "카페 명"; // 나중에 입력받는 형태 로 지정가능
    private int total_price = 0;

    // 카테고리 메뉴별 데이터(카테고리정보, 메뉴정보 포함)
    //private Timer timer; //

    public FE_KIOSK(){
        // 창 크기 설정 (1920x1020의 약 90%)
        setSize(1728, 918);
        
        // 화면 중앙에 창 위치시키기
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("키오스크");

        // 창 크기 지정 3개 컨테이너
        int left_width = (int) (getWidth() * 0.3);
        int mid_width = (int) (getWidth() * 0.3);
        int right_width = (int) (getWidth() * 0.3);
        

        // 전체 화면 컨테이너
        c = getContentPane();
        c.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 컴포넌트가 공간을 가득 채우도록 설정
        gbc.weighty = 1.0; // 세로 크기를 동일하게 설정



        
        left_c = new JPanel();
        left_c.setLayout(new BorderLayout());
        Dimension leftSize = new Dimension((int)(getWidth() * 0.25), getHeight());
        left_c.setPreferredSize(leftSize);
        left_c.setMaximumSize(leftSize);
        left_c.setMinimumSize(leftSize);

        mid_c = new JPanel();
        mid_c.setLayout(new BoxLayout(mid_c, BoxLayout.Y_AXIS));
        // 중간 컨테이너 크기 고정
        Dimension midSize = new Dimension((int)(getWidth() * 0.5), getHeight());
        mid_c.setPreferredSize(midSize);
        mid_c.setMaximumSize(midSize);
        mid_c.setMinimumSize(midSize);

        right_c = new JPanel(new BorderLayout());
        Dimension rightSize = new Dimension((int)(getWidth() * 0.25), getHeight());
        right_c.setPreferredSize(rightSize);
        right_c.setMaximumSize(rightSize);
        right_c.setMinimumSize(rightSize);

        //mid_c.setMaximumSize(new Dimension(Short.MAX_VALUE, mid_c.getPreferredSize().height));//????

        // 카테고리부분 왼쪽
        leftly_main = new JPanel();
        leftly_main.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        
        leftly = new JPanel();
        leftly.setLayout(new BoxLayout(leftly, BoxLayout.Y_AXIS)); // basic_load(leftly_main);

        leftly_down = new JPanel();
        leftly_down.setLayout(new FlowLayout(FlowLayout.RIGHT));



        
        
        // 메뉴 보이는 부분 간
        midly_main = new JPanel();
        midly_main.setLayout(new BoxLayout(midly_main, BoxLayout.Y_AXIS));//예비
        
        midly = new JPanel();
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 500));
        //midly.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        midly_menu = new JScrollPane(midly);
        midly_menu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        midly_menu.getVerticalScrollBar().setUnitIncrement(32);  // 스크롤 단위 증가 (기본값 1)
        midly_menu.getVerticalScrollBar().setBlockIncrement(256);  // 페이지 단위 증가 (기본값 10)

        midly_down = new JPanel();
        

        // 계산 하는 부분 오른쪽
        //rightly_main = new JPanel(); // 레이아웃 미정
        rightly_main = new JPanel();
        rightly_main.setLayout(new BoxLayout(rightly_main, BoxLayout.Y_AXIS));
        rightly = new JPanel();
        rightly.setLayout(new BoxLayout(rightly, BoxLayout.Y_AXIS));
        rightly_down = new JPanel();
        
        // rightly를 스크롤 패널에 추가
        JScrollPane rightlyScroll = new JScrollPane(rightly);
        rightlyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightlyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightlyScroll.setBorder(BorderFactory.createEmptyBorder());
        rightlyScroll.getVerticalScrollBar().setUnitIncrement(16);

        // rightly_down 초기화 (최하단)
        rightly_down = new JPanel();
        rightly_down.setLayout(new BoxLayout(rightly_down, BoxLayout.Y_AXIS));

        // 중앙 패널 생성 (rightly_main과 rightlyScroll을 포함)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(rightly_main, BorderLayout.NORTH);
        centerPanel.add(rightlyScroll, BorderLayout.CENTER);

        // right_c에 컴포넌트들 추가
        right_c.add(centerPanel, BorderLayout.CENTER);
        right_c.add(rightly_down, BorderLayout.SOUTH);

        left_c.add(leftly_main, BorderLayout.NORTH);
        left_c.add(leftly, BorderLayout.CENTER);
        left_c.add(leftly_down, BorderLayout.SOUTH);
        

        mid_c.add(midly_main);
        mid_c.add(midly_menu);
        mid_c.add(midly_down);
        
        //right_c.add(rightly_main);
        //right_c.add(rightlyScroll);  // rightly 대신 rightlyScroll 추가
        //right_c.add(rightly_down);

        gbc.gridx = 0;
        gbc.weightx = 0.325; // 30% 가로 비율
        c.add(left_c, gbc);
        //c.add(left_c);

        gbc.gridx = 1;
        gbc.weightx = 0.35; // 40% 가로 비율
        c.add(mid_c, gbc);
        //c.add(mid_c);

        gbc.gridx = 2;
        gbc.weightx = 0.325; // 30% 가로 비율
        c.add(right_c, gbc);
        //c.add(right_c);

        // 기본 데이터 로드
        basic_load(leftly_main, leftly_down);
        rotate_data();

        // 카테고리 버튼 추가
        //addcategorybt(leftly);

        setVisible(true);
    }

    public void show_success(int code) {
        System.out.println("success");
        
        // 알림 메시지 설정
        String message;
        if (code == 0) {
            message = "주문이 완료되었습니다";
        } else if (code == 1) {
            message = "계산대로 이동해주세요";
        } else {
            message = "요청되었습니다";
        }
        
        // 알림 패널 생성
        JPanel alertPanel = new JPanel();
        alertPanel.setBackground(new Color(0, 0, 0, 150));  // 반투명 검은색 배경
        alertPanel.setLayout(new GridBagLayout());  // 중앙 정렬을 위한 GridBagLayout
        
        // 메시지 라벨 생성
        JLabel alertLabel = new JLabel(message);
        alertLabel.setFont(new Font(alertLabel.getFont().getName(), Font.BOLD, 24));
        alertLabel.setForeground(Color.WHITE);
        alertLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // 알림 패널에 메시지 추가
        alertPanel.add(alertLabel);
        
        // 알림 패널을 화면 중앙에 추가
        alertPanel.setBounds(0, 0, getWidth(), getHeight());
        getLayeredPane().add(alertPanel, JLayeredPane.POPUP_LAYER);
        
        // 1초 후 알림 제거
        Timer timer = new Timer(1000, e -> {
            getLayeredPane().remove(alertPanel);
            getLayeredPane().revalidate();
            getLayeredPane().repaint();
        });
        timer.setRepeats(false);  // 타이머가 한 번만 실행되도록 설정
        timer.start();
    }

    public void show_check() {
        System.out.println("check");
        midly_main.removeAll();
        midly.removeAll();
        midly_down.removeAll();
        
        // Message 타이틀 추가
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("계산하기");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 40));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titlePanel.add(titleLabel);
        midly_main.add(titlePanel);

        // 메시지 담을 패널
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        
        // orderList_final의 각 항목을 표시
        for (List<Order_list> orderListItems : orderList_final.values()) {
            for (Order_list order : orderListItems) {
                try {
                    URL url = new URL(order.geturl());
                    
                    // 각 주문 항목을 위한 패널
                    JPanel itemPanel = new JPanel(new BorderLayout());
                    itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                    itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

                    // 왼쪽 패널 (이미지)
                    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(url)
                        .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
                    leftPanel.add(imageLabel);

                    // 중앙 패널 (메뉴 이름과 수량)
                    JPanel centerPanel = new JPanel();
                    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                    
                    JLabel nameLabel = new JLabel(order.getname());
                    nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 16));
                    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    JLabel quantityLabel = new JLabel(String.format("%d개", order.getnum()));
                    quantityLabel.setFont(new Font(quantityLabel.getFont().getName(), Font.PLAIN, 14));
                    quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    centerPanel.add(nameLabel);
                    centerPanel.add(Box.createVerticalStrut(5));
                    centerPanel.add(quantityLabel);
                    centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));

                    // 오른쪽 패널 (총 가격)
                    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                    JLabel priceLabel = new JLabel(String.format("%,d원", order.getprice() * order.getnum()));
                    priceLabel.setFont(new Font(priceLabel.getFont().getName(), Font.PLAIN, 14));
                    rightPanel.add(priceLabel);

                    // 패널에 컴포넌트 추가
                    itemPanel.add(leftPanel, BorderLayout.WEST);
                    itemPanel.add(centerPanel, BorderLayout.CENTER);
                    itemPanel.add(rightPanel, BorderLayout.EAST);

                    messageContainer.add(itemPanel);
                    messageContainer.add(Box.createVerticalStrut(10));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        // midly 패널 설정 수정
        midly.setLayout(new BorderLayout());
        midly.add(messageContainer, BorderLayout.NORTH); // CENTER에서 NORTH로 변경
        
        // 스크롤 패널 설정 수정
        midly_menu.setViewportView(midly);
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // midly의 크기 설정
        int containerHeight = orderList_final.values().stream()
            .mapToInt(List::size)
            .sum() * 120; // 각 아이템당 120픽셀 높이 할당
        
        // 최소 높이 설정 (600px)
        containerHeight = Math.max(containerHeight, 600);
        
        // midly의 선호 크기 설정
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, containerHeight));
        
        // messageContainer의 정렬 설정
        messageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        // 스크롤 패널 설정 수정
        midly_menu.setViewportView(midly);
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // 총 가격 계산
        int totalPrice = 0;
        for (List<Order_list> orderListItems : orderList_final.values()) {
            for (Order_list order : orderListItems) {
                totalPrice += order.getprice() * order.getnum();
            }
        }

        // 계산하기 버튼과 총 가격을 담을 패널
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 계산하기 버튼 (가로 300px, 세로 50px로 크기 조정)
        JButton calculateButton = new JButton("계산하기");
        calculateButton.setPreferredSize(new Dimension(300, 50));
        calculateButton.setMaximumSize(new Dimension(300, 50));
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 버튼 클릭 이벤트
        calculateButton.addActionListener(e -> {
            send_order_final();
            orderList_final.clear();
        });

        // 총 가격 라벨
        JLabel totalLabel = new JLabel(String.format("Total Price = %,d원", totalPrice));
        totalLabel.setFont(new Font(totalLabel.getFont().getName(), Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // 패널에 버튼과 라벨 추가
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(totalLabel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(calculateButton);
        bottomPanel.add(Box.createHorizontalStrut(20));

        // midly_down에 패널 추가
        midly_down.setLayout(new BoxLayout(midly_down, BoxLayout.Y_AXIS));
        midly_down.add(Box.createVerticalStrut(5));
        midly_down.add(bottomPanel);
        midly_down.add(Box.createVerticalStrut(5));

        // UI 갱신
        midly_main.revalidate();
        midly_main.repaint();
        midly.revalidate();
        midly.repaint();
        midly_down.revalidate();
        midly_down.repaint();
        midly_menu.revalidate();
        midly_menu.repaint();
    }

    private void show_menu(String category) {
        // midly_main에 카테고리 정보 업데이트
        
        
        // 스크롤 설정 초기화
        midly_main.removeAll();
        midly_down.removeAll();
        midly.removeAll();
        midly_down.setLayout(null);
        
        // 2. viewport 초기화 및 기본 설정
        midly_menu.setViewportView(null);
        midly.setLayout(new GridBagLayout());

        //midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 700));
        //midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        JLabel titleLabel = new JLabel(
            "<html>" + "<br><br>" +
            "<div style='text-align: left; margin-left: 20px;'>" +  // 왼쪽 정렬 및 여백 추가
            "<span style='font-size: 28px; font-weight: bold;'>" + category + "</span>" +
            "<br><br><br><br>" +
            "<span style='font-size: 18px;'>Choose Dishes</span>" +
            "<br><br>" +
            "</div>" +
            "</html>"
        );
        titleLabel.setHorizontalAlignment(JLabel.LEFT);  // JLabel 자체도 왼쪽 정렬
        midly_main.add(titleLabel);
        midly_main.revalidate();
        midly_main.repaint();

        // midly 패널 설정
        midly.removeAll();
        //midly_menu.removeAll();
        midly.setLayout(new GridBagLayout());
        midly_menu.setViewportView(midly);
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 600));
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //midly.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        //midly.setMinimumSize(new Dimension(midly.getPreferredSize().width, 700));
        midly_menu.setMinimumSize(null);
        midly_menu.setMaximumSize(null);
        midly_menu.setPreferredSize(new Dimension(midly.getPreferredSize().width, 600));


        // 스크롤 동작을 위한 추가 설정
        midly.setPreferredSize(new Dimension(
            midly.getPreferredSize().width,
            ((menuData.get(category).size() - 1) / 3 + 1) * 300
        ));

        

        midly_menu.setViewportView(midly);
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        
        List<Menu> menuList = menuData.get(category);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // 마지막 행에 가중치를 주기 위한 더미 컴포넌트 추가 위치 계산
        int lastRowIndex = (menuList.size() - 1) / 3;

        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);

            try {
                URL url = new URL(menu.imageUrl);
                JButton menubt = new JButton(
                    "<html>" + 
                    "<div style='text-align: center;'>" +
                    "<img src='" + url + "' width='170' height='150'><br><br>" +  // 이미지 크기 170x150으로 증가
                    "<span style='font-size: 16px;'>" + menu.name + "</span><br>" +  // 글자 크기 증가
                    "<span style='font-size: 14px;'>" + menu.price + "원</span>" +  // 가격 글자 크기 증가
                    "</div>" +
                    "</html>"
                );
                menubt.addActionListener(e -> {
                    System.out.println("클릭된 메뉴: " + menu.name);
                    addmenulist(rightly, menu.name, menu.price, menu.imageUrl, menu.id, 1);
                });
                menubt.setEnabled(menu.available);
                menubt.setPreferredSize(new Dimension(400, 400));  // 버튼 크기를 400x400으로 증가

                gbc.gridx = i % 3; // 열 위치
                gbc.gridy = i / 3; // 행 위치

                // 마지막 행의 포넌트인 경우 나머지 공간을 채우도록 정
                if (gbc.gridy == lastRowIndex) {
                    gbc.weighty = 1.0;
                } else {
                    gbc.weighty = 0.0;
                }

                midly.add(menubt, gbc);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        
        midly.revalidate();
        midly.repaint();
        midly_menu.revalidate();
        midly_menu.repaint();
    }

    public void show_text() {
        // 패널 초기화
        midly_main.removeAll();
        midly.removeAll();
        midly_down.removeAll();
        midly_down.setLayout(null);
        
        // Message 타이틀 추가
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(
            "<html>" +
            "요청하기" +
            "</html>"
            );
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 40));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titlePanel.add(titleLabel);
        midly_main.add(titlePanel);
    
        // 메시지를 담을 패널
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        
        // requestList의 각 항목을 버튼으로 표시
        for (Map.Entry<String, String[]> entry : requestList.entrySet()) {
            String id = entry.getKey();
            String name = entry.getValue()[0];
            String status = entry.getValue()[1];
            
            // 메시지 패널 생성
            JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            
            // 버튼 배경색 설정 (active 여부에 따라)
            String backgroundColor = status.equals("active") ? "#4F90FF" : "#808080";
            
            // HTML 스타일의 버튼 생성
            JButton messageButton = new JButton(
                "<html><div style='background: " + 
                backgroundColor + "; " +
                "color: white; " +
                "padding: 15px 25px; " +
                "border-radius: 20px; " +
                "margin: 10px 20px; " +
                "font-family: Arial; " +
                "font-size: 14px; " +
                "max-width: 400px; " +
                "word-wrap: break-word;'>" +
                name +
                "</div></html>"
            );
            
            // 버튼 스타일 설정
            messageButton.setBorderPainted(false);
            messageButton.setContentAreaFilled(false);
            messageButton.setFocusPainted(false);
            
            // active 상태에 따라 버튼 활성화/비활성화 및 커서 설정
            if (status.equals("active")) {
                messageButton.setEnabled(true);
                messageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                messageButton.setEnabled(false);
                messageButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            // 버튼 클릭 이벤트 추가
            messageButton.addActionListener(e -> {
                send_request(id, name);
            });
            
            messagePanel.add(messageButton);
            messageContainer.add(messagePanel);
            messageContainer.add(Box.createVerticalStrut(5));  // 버튼 사이 간격
        }
    
        // midly 설정
        midly.setLayout(new BorderLayout());
        midly.add(messageContainer, BorderLayout.CENTER);
        
        // midly_menu 스크롤 설정
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        midly_menu.getVerticalScrollBar().setUnitIncrement(16);
    
        // UI 갱신
        midly_main.revalidate();
        midly_main.repaint();
        midly.revalidate();
        midly.repaint();
        midly_down.revalidate();
        midly_down.repaint();
    }

    public void send_request(String id, String name) {
        try {
            // JSON 객체 생성
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("tableNum", table_num);
            
            // requests 배열 생성
            JsonArray requestsArray = new JsonArray();
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("id", id);
            requestObject.addProperty("name", name);
            requestsArray.add(requestObject);
            
            jsonRequest.add("requests", requestsArray);

            // 보낼 JSON 데이터 출력
            System.out.println("전송할 JSON 데이터:");
            System.out.println(jsonRequest.toString());
            System.out.println("전송 URL: https://be-api-takaaaans-projects.vercel.app/api/request");
            
            // API 요청 보내기
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonRequest.toString());
            
            Request request = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/request")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
                
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                System.out.println("요청이 성공적으로 전송되었습니다.");
                show_success(2);
            } else {
                System.out.println("요청 전송 실패: " + response.code());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("요청 전송 중 오류 발생: " + e.getMessage());
        }
    }

    private void show_menu_list(JPanel rightly) {
        rightly.removeAll();
        rightly.setLayout(new BoxLayout(rightly, BoxLayout.Y_AXIS));

        for (List<Order_list> orderListItems : orderList.values()) {
            for (Order_list order : orderListItems) {
                try {
                    URL url = new URL(order.geturl());
                    int total = order.getprice() * order.getnum();

                    // 메인 패널
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                    itemPanel.setPreferredSize(new Dimension(400, 180));  // 높이를 160에서 180으로 증가
                    itemPanel.setMaximumSize(new Dimension(400, 180));    // 최대 크기도 같이 조정
                    itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // 상하 여백 추가

                    // 상단 패널
                    JPanel topPanel = new JPanel(new BorderLayout(10, 0));
                    
                    // 이미지
                    JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(url).getImage()
                        .getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                    
                    // 중앙 정보 패널
                    JPanel infoPanel = new JPanel(new GridLayout(2, 1));
                    JLabel nameLabel = new JLabel(order.getname());
                    JLabel priceLabel = new JLabel(order.getprice() + "원");
                    infoPanel.add(nameLabel);
                    infoPanel.add(priceLabel);

                    // 총액 라벨 패널 수정
                    JPanel totalPanel = new JPanel();
                    totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
                    
                    JLabel totalLabel = new JLabel(total + "");
                    totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 가운데 정렬
                    totalPanel.add(Box.createVerticalGlue());  // 상단 여백
                    totalPanel.add(totalLabel);
                    totalPanel.add(Box.createVerticalGlue());  // 하단 여백

                    topPanel.add(imageLabel, BorderLayout.WEST);
                    topPanel.add(infoPanel, BorderLayout.CENTER);
                    topPanel.add(totalPanel, BorderLayout.EAST);

                    // 하 패널
                    JPanel bottomPanel = new JPanel(new BorderLayout());
                    bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));  // 하단 여백 10으로 소
                    
                    // 수량 조절 패널 (왼쪽)
                    JPanel quantityPanel = new JPanel();
                    quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));

                    // 버튼 크기 정의
                    Dimension buttonSize = new Dimension(50, 50);

                    // 수량 라벨
                    JLabel quantityLabel = new JLabel(String.valueOf(order.getnum()));
                    quantityLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                    // 총 가격 라벨
                    JLabel totalPriceLabel = new JLabel(String.format("%d원", order.getprice() * order.getnum()));

                    // 마이너스 버튼
                    JButton minusButton = new JButton("-");
                    minusButton.setPreferredSize(new Dimension(50, 50));
                    minusButton.addActionListener(e -> {
                        int currentQuantity = order.getnum();
                        if (currentQuantity > 1) {
                            order.set_num(currentQuantity - 1);
                            show_menu_list(rightly);  // 전체 리스트 갱신
                        }
                    });

                    // 플러스 버튼
                    JButton plusButton = new JButton("+");
                    plusButton.setPreferredSize(new Dimension(50, 50));
                    plusButton.addActionListener(e -> {
                        int currentQuantity = order.getnum();
                        if (currentQuantity < 99) {
                            order.set_num(currentQuantity + 1);
                            show_menu_list(rightly);  // 전체 리스트 갱신
                        }
                    });

                    quantityPanel.add(minusButton);
                    quantityPanel.add(quantityLabel);
                    quantityPanel.add(plusButton);

                    // 삭제 버튼 패널 (오른쪽)
                    JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton deleteButton = new JButton("삭제");
                    deleteButton.setPreferredSize(new Dimension(60, 50));
                    deleteButton.addActionListener(e -> {
                        orderList.remove(order.getid());
                        show_menu_list(rightly);
                    });
                    deletePanel.add(deleteButton);

                    bottomPanel.add(quantityPanel, BorderLayout.WEST);
                    bottomPanel.add(deletePanel, BorderLayout.EAST);

                    itemPanel.add(topPanel);
                    itemPanel.add(Box.createVerticalStrut(20));  // 20픽셀 간격
                    itemPanel.add(bottomPanel);

                    rightly.add(itemPanel);
                    rightly.add(Box.createVerticalStrut(5));  // 메뉴 간 간격 5로 소

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        total_price();

        right_c.revalidate();
        right_c.repaint();
    }

    public void total_price(){
        // rightly_down 패널에 총 가격 표시 여기 삭제제
        rightly_down.removeAll();
        rightly_down.setLayout(new BoxLayout(rightly_down, BoxLayout.Y_AXIS));

        // orderList에서 직접 총 가격 계산
        int totalPrice = 0;
        for (List<Order_list> orderListItems : orderList.values()) {
            for (Order_list order : orderListItems) {
                totalPrice += order.getprice() * order.getnum();
            }
        }

        // 총 가격 표시
        JLabel totalPriceLabel = new JLabel(String.format("Total Price %,d원", totalPrice));
        totalPriceLabel.setFont(new Font(totalPriceLabel.getFont().getName(), Font.BOLD, 30));
        totalPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 주문하기 튼 먼저 생성
        JButton orderButton = new JButton( // 버튼 스타일링
            "<html>" +
            "주문하기" +
            "</html>");
        orderButton.setPreferredSize(new Dimension(400, 50));
        orderButton.setMaximumSize(new Dimension(400, 50));
        orderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderButton.setFont(new Font(orderButton.getFont().getName(), orderButton.getFont().getStyle(), 20));

        // 주문하기 버튼 클릭 이벤트
        orderButton.addActionListener(e -> {
            System.out.println("주문하기 버튼 클릭됨");
            //System.out.println("총 결제 금액: " + totalPrice + "원");
            send_order();  // API 요청 전송
            
            // 주문 완료 후 장바구니 비기
            orderList.clear();
            show_menu_list(rightly);  // UI 갱신
        });
        rightly_down.add(Box.createVerticalStrut(10));
        rightly_down.add(totalPriceLabel);
        rightly_down.add(Box.createVerticalStrut(10));
        rightly_down.add(orderButton);
        rightly_down.add(Box.createVerticalStrut(10));

        rightly_down.revalidate();
        rightly_down.repaint();
    }


    // 장구니 리스트 표기
    public void addmenulist(JPanel rightly, String name, int price, String imageUrl, String id, int num){
        if (orderList.containsKey(id)){
            List<Order_list> renew_order = orderList.get(id);
            
            for (Order_list order : renew_order) {
                if (order.getid().equals(id)) {
                    order.set_num(order.getnum() + num); // num을 증가
                    show_menu_list(rightly); // 변경 사항을 반영하기 위해 show_menu_list 호출
                    return;
                }
            }
        } else {
            Order_list list = new Order_list(name, price, imageUrl, id, 1);
            orderList.putIfAbsent(id, new ArrayList<>());
            orderList.get(id).add(list);
        }
        show_menu_list(rightly);
    }


    public void load_data(){//  
        
    }

    public void conn_be() {
        OkHttpClient client = new OkHttpClient();
        
        // 기존 메뉴 데이터 요청
        Request menuRequest = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/menu?available=true")
                .build();

        // 새로운 요청 목록 데이��� 요청
        Request requestListRequest = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/request/list")
                .build();

        try {
            // 메뉴 데이터 처리
            try (Response menuResponse = client.newCall(menuRequest).execute()) {
                String menuResponseBody = menuResponse.body().string();
                JsonObject jsonResponse = new JsonParser().parse(menuResponseBody).getAsJsonObject();
                JsonArray menuItemsArray = jsonResponse.getAsJsonArray("menuItems");

                Map<String, List<Menu>> newMenuData = new LinkedHashMap<>();
                // 기존 메뉴 데이터 처리 로직...
                for (JsonElement element : menuItemsArray) {
                    // 기존 코드 유지
                    JsonObject menuItemObj = element.getAsJsonObject();
                    String id = menuItemObj.get("_id").getAsString();
                    String name = menuItemObj.get("name").getAsString();
                    int price = menuItemObj.get("price").getAsInt();
                    String imageUrl = menuItemObj.get("imageUrl").getAsString();
                    boolean available = menuItemObj.get("available").getAsBoolean();
                    String category = menuItemObj.get("category").getAsString();

                    Menu menu = new Menu(id, name, price, imageUrl, available, category);
                    newMenuData.putIfAbsent(category, new ArrayList<>());
                    newMenuData.get(category).add(menu);
                }

                // 데이터 비교 로직
                boolean isSameData = true;
                // 기존 비교 로직 유지...
                for (String category : newMenuData.keySet()) {
                    List<Menu> newMenuList = newMenuData.get(category);
                    List<Menu> oldMenuList = menuData.get(category);
                    
                    if (oldMenuList == null || oldMenuList.size() != newMenuList.size()) {
                        isSameData = false;
                        break;
                    }
                    
                    for (int i = 0; i < newMenuList.size(); i++) {
                        Menu newMenu = newMenuList.get(i);
                        Menu oldMenu = oldMenuList.get(i);
                        
                        if (!newMenu.id.equals(oldMenu.id) || 
                            !newMenu.name.equals(oldMenu.name) || 
                            newMenu.price != oldMenu.price || 
                            !newMenu.imageUrl.equals(oldMenu.imageUrl) || 
                            newMenu.available != oldMenu.available || 
                            !newMenu.category.equals(oldMenu.category)) {
                            isSameData = false;
                            break;
                        }
                    }
                }

                if (!isSameData) {
                    menuData.clear();
                    menuData.putAll(newMenuData);

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            addcategorybt(leftly);
                            if (!menuData.isEmpty()) {
                                String firstCategory = menuData.keySet().iterator().next();
                                show_menu(firstCategory);
                            }
                        }
                    });
                }
            }

            // 요청 목록 데이터 처리
            try (Response requestListResponse = client.newCall(requestListRequest).execute()) {
                String requestListResponseBody = requestListResponse.body().string();
                JsonObject requestListJson = new JsonParser().parse(requestListResponseBody).getAsJsonObject();
                
                // 응답이 성공적인지 확인
                if (requestListJson.get("success").getAsBoolean()) {
                    JsonArray requestsArray = requestListJson.getAsJsonArray("data");
                    
                    // requestList 맵 초기화
                    requestList.clear();
                    
                    // 새로운 데이터 저장
                    for (JsonElement element : requestsArray) {
                        JsonObject request = element.getAsJsonObject();
                        String id = request.get("_id").getAsString();
                        String name = request.get("name").getAsString();
                        boolean isActive = request.get("isActive").getAsBoolean();
                        
                        // requestList에 데이터 저장
                        // [0]: 이름, [1]: 활성상태("active" 또는 "inactive")
                        requestList.put(id, new String[]{
                            name,
                            isActive ? "active" : "inactive"
                        });
                    }

                    // requestList 내용 출력
                    System.out.println("\n=== 저장된 요청 목록 ===");
                    for (Map.Entry<String, String[]> entry : requestList.entrySet()) {
                        System.out.printf("ID: %s\n요청내용: %s\n상태: %s\n---\n", 
                            entry.getKey(), 
                            entry.getValue()[0], 
                            entry.getValue()[1]);
                    }
                    System.out.println("====================\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }     
    }

    public void send_order_final() {
        try {
            // orderList가 비어있는지 확인
            if (orderList_final.isEmpty()) {
                System.out.println("주문 목록이 비어있습니다.");
                return;  // 메소드 종료
            }

            // JSON 객체 생성
            JsonObject jsonOrder = new JsonObject();
            JsonArray orderItems = new JsonArray();
            
            // 총 가격 계산
            int totalPrice = 0;
            
            // orderList_fianl의 각 아이템을 JSON 형식으로 변환
            for (List<Order_list> orderListItems : orderList_final.values()) {
                for (Order_list order : orderListItems) {
                    JsonObject item = new JsonObject();
                    item.addProperty("menuId", order.getid());
                    item.addProperty("name", order.getname());
                    item.addProperty("quantity", order.getnum());
                    item.addProperty("price", order.getprice()*order.getnum());
                    
                    // 개별 주문의 총 가격 계산
                    totalPrice += order.getprice() * order.getnum();

                    Integer[] values = check.containsKey(order.getname()) ? 
                        check.get(order.getname()) : new Integer[]{0, 0};
                    values[0] += order.getnum();
                    values[1] += order.getprice() * order.getnum();
                    
                    orderItems.add(item);
                }
            }
            
            // JSON 객체에 데이터 추가
            jsonOrder.add("orderItems", orderItems);
            jsonOrder.addProperty("totalPrice", totalPrice);
            
            // 보낼 JSON 데이터 출력
            System.out.println("전송할 JSON 데이터:");
            System.out.println(jsonOrder.toString());
            System.out.println("전송 URL: https://be-api-takaaaans-projects.vercel.app/api/table/new_order?tableNum=" + table_num);
            
            // API 요청 부분 주석 처리
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonOrder.toString());
            
            Request request = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/table/new_order?tableNum=" + table_num)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
                
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                System.out.println("주문이 성공적으로 전송되었니다.");
                orderList_final.clear();
                show_success(1);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        show_menu(menuData.keySet().iterator().next());
                    
                    }
                    }
                    );
                
            } else {
                System.out.println("주문 전송 실패: " + response.code());
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("주문 전송 중 오류 발생: " + e.getMessage());
        }
    }

    public void send_order() {
        try {
            // orderList가 비어있는지 확인
            if (orderList.isEmpty()) {
                System.out.println("주문 목록이 비어있습니다.");
                return;  // 메소드 종료
            }

            // JSON 객체 생성
            JsonObject jsonOrder = new JsonObject();
            JsonArray orderItems = new JsonArray();
            
            // 총 가격 계산
            int totalPrice = 0;
            
            // orderList의 각 아이템을 JSON 형식으로 변환
            for (List<Order_list> orderListItems : orderList.values()) {
                for (Order_list order : orderListItems) {
                    JsonObject item = new JsonObject();
                    item.addProperty("menuId", order.getid());
                    item.addProperty("name", order.getname());
                    item.addProperty("quantity", order.getnum());
                    item.addProperty("price", order.getprice()*order.getnum());
                    
                    // 개별 주문의 총 가격 계산
                    totalPrice += order.getprice() * order.getnum();

                    Integer[] values = check.containsKey(order.getname()) ? 
                        check.get(order.getname()) : new Integer[]{0, 0};
                    values[0] += order.getnum();
                    values[1] += order.getprice() * order.getnum();
                    
                    // orderList_final에 항목 추가 또는 업데이트
                    if (orderList_final.containsKey(order.getid())) {
                        List<Order_list> existingOrders = orderList_final.get(order.getid());
                        boolean found = false;
                        for (Order_list existingOrder : existingOrders) {
                            if (existingOrder.getid().equals(order.getid())) {
                                // 기존 주문이 있으면 수량과 가격 업데이트
                                existingOrder.set_num(existingOrder.getnum() + order.getnum());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            // 같은 id지만 다른 주문이면 새로 추가
                            Order_list list = new Order_list(
                                order.getname(), 
                                order.getprice(), 
                                order.geturl(), 
                                order.getid(), 
                                order.getnum()
                            );
                            existingOrders.add(list);
                        }
                    } else {
                        // 새로운 메뉴면 새 리스트 생성
                        Order_list list = new Order_list(
                            order.getname(), 
                            order.getprice(), 
                            order.geturl(), 
                            order.getid(), 
                            order.getnum()
                        );
                        orderList_final.putIfAbsent(order.getid(), new ArrayList<>());
                        orderList_final.get(order.getid()).add(list);
                    }
                    
                    orderItems.add(item);
                }
            }
            
            // JSON 객체에 데이터 추가
            jsonOrder.add("orderItems", orderItems);
            jsonOrder.addProperty("totalPrice", totalPrice);
            
            // 보낼 JSON 데이터 출력
            System.out.println("전송할 JSON 데이터:");
            System.out.println(jsonOrder.toString());
            System.out.println("전송 URL: https://be-api-takaaaans-projects.vercel.app/api/table/new_order?tableNum=" + table_num);
            
            // API 요청 부분 주석 처리
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonOrder.toString());
            
            Request request = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/table/new_order?tableNum=" + table_num)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
                
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                System.out.println("주문이 성공적으로 전송되었니다.");
                // 주문 성공 시 check 채우기 확인
                /*
                check.forEach((menu, total) -> 
                    System.out.println(menu + ": " + total + "원"));
                    */
                // 주문 성공 시 orderList 비우기
                orderList.clear();
                show_success(0);
            } else {
                System.out.println("주문 전송 실패: " + response.code());
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("주문 전송 중 오류 발생: " + e.getMessage());
        }
    }

    //카테고리 leftly에 동적 추가하는 부분 (처음에 leftly 부분 초기화후 새로고침시 다시 작하게 하면 동적 수정 가능)
    private void addcategorybt(JPanel leftly) {
        leftly.removeAll();
        //leftly.setBorder(BorderFactory.createLineBorder(Color.RED, 2));// 테두리 확인용
        leftly.setLayout(new BoxLayout(leftly, BoxLayout.Y_AXIS));
        
        // 상단 여백 추가
        leftly.add(Box.createVerticalStrut(20));
        
        for (String category : menuData.keySet()) {
            // 버튼을 감싸는 패널 생성
            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.setPreferredSize(new Dimension(300, 70));
            buttonPanel.setMaximumSize(new Dimension(300, 70));
            
            // 버튼 생성
            JButton categoryButton = new JButton();
            categoryButton.setLayout(new BorderLayout());
            
            // 텍스트를 오른쪽 정렬로 설정하고 크기 키우기
            JLabel textLabel = new JLabel(category);
            textLabel.setHorizontalAlignment(JLabel.RIGHT);
            textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
            textLabel.setFont(textLabel.getFont().deriveFont(25.0f));  // 기존 폰트의 크만 18로 키우
            
            // 구분선 추가
            JSeparator separator = new JSeparator();
            separator.setForeground(new Color(255, 99, 71));
            
            // 버튼에 컴포넌트 추가
            categoryButton.add(textLabel, BorderLayout.CENTER);
            
            // 버튼 스타일링
            categoryButton.setBorderPainted(false);
            categoryButton.setContentAreaFilled(false);
            categoryButton.setFocusPainted(false);
            
            // 클릭 이벤트 추가
            categoryButton.addActionListener(e -> {
                System.out.println("카테리 버튼 클릭: " + category);
                show_menu(category);
            });
            
            // 패널에 버튼과 구분선 추가
            buttonPanel.add(categoryButton, BorderLayout.CENTER);
            buttonPanel.add(separator, BorderLayout.SOUTH);
            
            // leftly에 패널 추가
            leftly.add(buttonPanel);
            
            // 버튼 사이 여백 추가
            leftly.add(Box.createVerticalStrut(15));
        }
        
        // 남은 공간을 채우기 위한 빈 패널 추가
        leftly.add(Box.createVerticalGlue());
        
        leftly.revalidate();
        leftly.repaint();
    }
    


    public class Order_list{
        private String name;
        private int price;
        private String imageUrl;
        private String id;
        private int num;

        public Order_list(String name, int price, String imageUrl, String id, int num){
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.id = id;
            this.num = num;
        }

        public void set_num(int num){
            this.num = num;
        }

        public String getid(){
            return id;
        }

        public int getnum(){
            return num;
        }

        public String geturl(){
            return imageUrl;
        }

        public String getname(){
            return name;
        }

        public int getprice(){
            return price;
        }

    }

    // menu class
    static class Menu{
        private String id;
        private String name;
        int price;
        String imageUrl;
        boolean available;
        String category;

        public Menu(String id, String name, int price, String imageUrl, boolean available, String category){
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.available = available;
            this.category = category;
        }
    }

    private void rotate_data(){
        conn_be();
        Timer timer = new Timer(30000, e -> {
            new Thread(() -> {
                conn_be();
                System.out.println("데이터 로딩 완료");
            }).start();
        });
        timer.start();
    }

    public void fix_table(){
        System.out.println("hi");
        // 입력 다이얼로그 생성
        JDialog dialog = new JDialog(this, "테이블 번호 변경", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        // 입력 패널 생성
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("새로운 테이블 번호: ");
        JTextField textField = new JTextField(5);
        inputPanel.add(label);
        inputPanel.add(textField);

        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");

        // 확인 버튼 이벤트
        confirmButton.addActionListener(e -> {
            try {
                int newTableNum = Integer.parseInt(textField.getText().trim());
                if (newTableNum > 0) {
                    table_num = newTableNum;
                    dialog.dispose();
                    // 테이블 번호가 변경되었으므로 UI 업데이트
                    basic_load(leftly_main, leftly_down);
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "1 이상의 숫자를 입력해주세요.", 
                        "입력 오류", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "올바른 숫자를 입력해주세요.", 
                    "입력 오류", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // 취소 버튼 이벤트
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // 다이얼로그에 컴포넌트 추가
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Enter 키로 확인 버튼 클릭
        textField.addActionListener(e -> confirmButton.doClick());

        dialog.setVisible(true);
    }

    private void basic_load(JPanel leftly_main, JPanel leftly_down) {
        // HTML을 사용한 스타일링
        String htmlContent = String.format("<html>" +
            "<div style='text-align: center; margin-right: 20px;'>" +  // margin-right 추가
            "<h1 style='font-size: 24px; color: #333;'>%s</h1>" +
            "<p style='font-size: 16px; color: #666;'>%s</p>" +
            "</div>" +
            "</html>", 
            store_name,  
            new SimpleDateFormat("yyyy - MM - dd").format(new Date())
        );

        // HTML 컨텐츠를 표시할 JLabel 생성
        JLabel contentLabel = new JLabel(htmlContent);
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // leftly_main 패널에 라벨 추가
        leftly_main.removeAll();
        leftly_main.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));  // 왼쪽 여백 추가
        leftly_main.add(contentLabel);
        
        // 레이아웃 갱신
        leftly_main.revalidate();
        leftly_main.repaint();

        // leftly_down 패널에 버튼 추가
        leftly_down.removeAll();
        leftly_down.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton homeButton = new JButton("Home");
        JButton textButton = new JButton("요청하기");
        JButton checkButton = new JButton("계산하기");

        Dimension buttonSize = new Dimension(100, 60); // 너비 200, 높이 50
        homeButton.setPreferredSize(buttonSize);
        textButton.setPreferredSize(buttonSize);
        checkButton.setPreferredSize(buttonSize);

        Font buttonFont = new Font(homeButton.getFont().getName(), Font.BOLD, 15); // Arial, Bold, 크기 18
        homeButton.setFont(buttonFont);
        textButton.setFont(buttonFont);
        checkButton.setFont(buttonFont);

        // 버튼 클릭 이벤트 추가
        homeButton.addActionListener(e -> {
            System.out.println("홈으로 돌아가기");
            
            // midly 관련 패널 초기화
            midly_main.removeAll();
            midly.removeAll();
            midly_down.removeAll();

            // 첫 번째 카테고리의 메뉴 표시 (초기 화면)
            if (!menuData.isEmpty()) {
                String firstCategory = menuData.keySet().iterator().next();
                show_menu(firstCategory);
            }

            // UI 갱신
            midly_main.revalidate();
            midly_main.repaint();
            midly.revalidate();
            midly.repaint();
            midly_down.revalidate();
            midly_down.repaint();
        });

        textButton.addActionListener(e -> {
            System.out.println("hi");
            show_text();
        });

        checkButton.addActionListener(e -> {
            System.out.println("hi");
            show_check();
        });

        leftly_down.add(homeButton);
        leftly_down.add(textButton);
        leftly_down.add(checkButton);

        // 레이아웃 갱신
        leftly_down.revalidate();
        leftly_down.repaint();

        // 주문쪽 상시표시
        total_price();

        // rightly_main 초기화 및 설정
        rightly_main.removeAll();
        rightly_main.setLayout(new BoxLayout(rightly_main, BoxLayout.Y_AXIS));
        rightly_main.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // 테이블 번호 패널 (먼저 추가)
        JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel tableLabel = new JLabel("Table " + table_num);
        tableLabel.setFont(new Font(tableLabel.getFont().getName(), Font.BOLD, 24));
        tablePanel.add(tableLabel);

        // 테이블 번호 버튼
        JButton tableButton = new JButton("FIX");
        tableButton.addActionListener(e -> {
            fix_table();
        });
        tablePanel.add(tableButton);

        // Dine In 버튼 패널 (그 다음 추가)
        JPanel dineInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton dineInButton = new JButton("Dine In");
        dineInButton.setBackground(new Color(255, 99, 71));
        dineInButton.setForeground(Color.WHITE);
        dineInButton.setBorderPainted(false);
        dineInButton.setFocusPainted(false);
        dineInPanel.add(dineInButton);

        // Item과 Price 라벨
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // 왼 패널 (Item)
        JLabel itemLabel = new JLabel("Item");
        itemLabel.setFont(new Font(itemLabel.getFont().getName(), Font.PLAIN, 20));
        
        // 오른쪽 패널 (Qty와 Price)
        JPanel rightLabels = new JPanel();
        rightLabels.setLayout(new GridLayout(1, 2));  // 1 2열의 GridLayout 사용
        rightLabels.setPreferredSize(new Dimension(250, rightLabels.getPreferredSize().height));  // 너비를 200에서 250으로 증가
        
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));  // 오른쪽 정렬, 간격 20
        JLabel qtyLabel = new JLabel("Qty");
        qtyLabel.setFont(new Font(qtyLabel.getFont().getName(), Font.PLAIN, 20));
        qtyPanel.add(qtyLabel);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));  // 오른쪽 정렬, 간격 20
        JLabel priceLabel = new JLabel("Price");
        priceLabel.setFont(new Font(priceLabel.getFont().getName(), Font.PLAIN, 20));
        pricePanel.add(priceLabel);
        
        rightLabels.add(qtyPanel);
        rightLabels.add(pricePanel);

        // 라벨 패널에 컴포넌트 추가
        labelPanel.add(itemLabel, BorderLayout.WEST);
        labelPanel.add(rightLabels, BorderLayout.EAST);

        // 구분선
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));

        // rightly_main에 컴포넌트 추가 (순서 변경)
        rightly_main.add(tablePanel);
        rightly_main.add(Box.createVerticalStrut(10));
        rightly_main.add(dineInPanel);
        rightly_main.add(Box.createVerticalStrut(20));
        rightly_main.add(labelPanel);
        rightly_main.add(Box.createVerticalStrut(5));
        rightly_main.add(separator);

        rightly_main.revalidate();
        rightly_main.repaint();
    }

    public static void main(String[] args){
        FE_KIOSK kiosk = new FE_KIOSK();
        //kiosk.conn_be();  
    }   
}

