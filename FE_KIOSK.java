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
    private int table_num = 2;
    private Map<String, List<Menu>> menuData = new LinkedHashMap<>();
    private Map<String, List<Order_list>> orderList = new LinkedHashMap<>();
    private Container c;
    private Container left_c;
    private Container mid_c;
    private Container right_c;
    private JPanel leftly_main;
    private JPanel leftly;
    private JPanel leftly_down;
    private JPanel left_L;
    private JPanel midly_main;
    private JPanel midly;
    private JPanel midly_down;
    private JScrollPane midly_menu;
    private JPanel rightly_main;
    private JPanel rightly;
    private JPanel rightly_down;
    String store_name = "카페 명"; // 나중에 입력받는 형태 로 지정가능
    private int total_price = 0;
    private List<String> messageHistory = new ArrayList<>();  // 메시지 기록 저장용

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

        //left_L = new JPanel(new GridBagLayout());



        
        
        // 메뉴 보이는 부분 간
        midly_main = new JPanel();
        midly_main.setLayout(new BoxLayout(midly_main, BoxLayout.Y_AXIS));//예비
        
        midly = new JPanel();
        //midly.setLayout(new GridBagLayout());//예비
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 500));

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

    private void show_menu(String category) {
        // midly_main에 카테고리 정보 업데이트
        midly_main.removeAll();
        midly_down.removeAll();
        
        // 스크롤 설정 초기화
        midly.setPreferredSize(null);
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
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
        midly.setLayout(new GridBagLayout());
        
        // 스크롤 동작을 위한 추가 설정
        midly.setPreferredSize(new Dimension(
            midly.getPreferredSize().width,
            ((menuData.get(category).size() - 1) / 3 + 1) * 300
        ));

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
    }

    public void show_text() {
        // 패널 초기화
        midly_main.removeAll();
        midly.removeAll();
        midly_down.removeAll();
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 800));  // 패널 높이를 800으로 설정
        midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        // Message 타이틀 추가
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Message");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 28));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titlePanel.add(titleLabel);
        midly_main.add(titlePanel);

        // 메시지를 담을 패널
        JPanel messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        
        // 스크롤 패널 설정
        JScrollPane scrollPane = new JScrollPane(messageContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);  // 테두리 제거
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  // 스크롤 속도 조정
        
        // midly 패널 설정
        midly.setLayout(new BorderLayout());
        midly.add(scrollPane, BorderLayout.CENTER);

        // 전송 버튼 먼저 생성
        JButton sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(100, 40));

        // 텍스트 입력 필드
        JTextField messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(400, 40));
        
        // 엔터키 이벤트 추가
        messageField.addActionListener(e -> {
            sendButton.doClick(); // 엔터키를 누르면 send 버튼 클릭과 동일한 효과
        });

        // 전송 버튼 클릭 이벤트
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                JLabel messageLabel = new JLabel(
                    "<html><div style='background: #4F90FF; " +
                    "color: white; " +
                    "padding: 15px 25px; " +
                    "border-radius: 20px; " +
                    "margin: 10px 20px; " +
                    "font-family: Arial; " +
                    "font-size: 14px; " +
                    "max-width: 400px; " +
                    "word-wrap: break-word;'>" +
                    message +
                    "</div></html>"
                );
                messagePanel.add(messageLabel);
                messageContainer.add(messagePanel);  // messageContainer에 추가
                messageField.setText("");
                
                // 스크롤을 최하단으로 이동
                SwingUtilities.invokeLater(() -> {
                    JScrollBar vertical = scrollPane.getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());
                });
                
                // UI 갱신
                messageContainer.revalidate();
                messageContainer.repaint();
                SwingUtilities.invokeLater(() -> {
                    messageField.requestFocusInWindow();
                });

                // send request 보내는 부분
                send_message(message);
            }
        });

        midly_down.add(messageField);
        midly_down.add(sendButton);

        // 텍스트 필드에 자동 포커스
        SwingUtilities.invokeLater(() -> {
            messageField.requestFocusInWindow();
        });

        // 메시지가 추가된 후 스크롤바 필요 여부 확인
        midly.revalidate();
        if (midly.getPreferredSize().height > 800) {
            midly_menu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            
            // 스크롤을 최하단으로 이동
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = midly_menu.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }

        // UI 갱신
        midly_main.revalidate();
        midly_main.repaint();
        midly.revalidate();
        midly.repaint();
        midly_down.revalidate();
        midly_down.repaint();
    }

    public void send_message(String message) { // 백엔드 확인 필요
        try {
            // JSON 객체 생성
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("tableNum", table_num);
            
            JsonArray requestsArray = new JsonArray();
            JsonObject requestObject = new JsonObject();
            //requestObject.addProperty("id", "64f1c8e5678abc12345d9f01");  // 고정된 ID 사용
            requestObject.addProperty("name", message);
            requestsArray.add(requestObject);
            
            jsonRequest.add("requests", requestsArray);

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
                System.out.println("메시지 전송 성공");
            } else {
                System.out.println("메시지 전송 실패: " + response.code());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메시지 전송 중 오류 발생: " + e.getMessage());
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
                    deleteButton.setPreferredSize(buttonSize);
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
                    rightly.add(Box.createVerticalStrut(5));  // 메뉴 간 간격 5로 감소

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
        //totalPriceLabel.setFont(new Font(totalPriceLabel.getFont().getName(), Font.BOLD, 16));
        totalPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 결제하기 버튼
        JButton orderButton = new JButton("결제하기");
        orderButton.setPreferredSize(new Dimension(150, 50));
        orderButton.setMaximumSize(new Dimension(150, 50));
        orderButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 결제하기 버튼 클릭 이벤트
        orderButton.addActionListener(e -> {
            System.out.println("결제하기 버튼 클릭됨");
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


    // 장바구니 리스트 표기
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
        Request request = new Request.Builder()
                .url("https://be-api-takaaaans-projects.vercel.app/api/menu?available=true")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonObject jsonResponse = new JsonParser().parse(responseBody).getAsJsonObject();
            JsonArray menuItemsArray = jsonResponse.getAsJsonArray("menuItems");

            
            Map<String, List<Menu>> newMenuData = new LinkedHashMap<>();
            // 새 데이터 처리
            for (JsonElement element : menuItemsArray) {
                JsonObject menuItemObj = element.getAsJsonObject();
                String id = menuItemObj.get("_id").getAsString();
                String name = menuItemObj.get("name").getAsString();
                int price = menuItemObj.get("price").getAsInt();
                String imageUrl = menuItemObj.get("imageUrl").getAsString();
                boolean available = menuItemObj.get("available").getAsBoolean();
                String category = menuItemObj.get("category").getAsString();

                // Menu 객체 생성
                Menu menu = new Menu(id, name, price, imageUrl, available, category);

                // 카고리별 메뉴 리스트에 추가
                newMenuData.putIfAbsent(category, new ArrayList<>());
                newMenuData.get(category).add(menu);
            }

            // 여기에 
            boolean isSameData = true;
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
            if (isSameData) {
                System.out.println("메뉴 데이터가 최신 상태입니다.");
                return;
            }

            menuData.clear();
            menuData.putAll(newMenuData);
            

            // 데이터 로딩이 완료된 후 EDT에서 UI 업데이트
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // 카테고리 버튼 추가
                    addcategorybt(leftly);
                    
                    //  번째 카테고의 메뉴 표시
                    if (!menuData.isEmpty()) {
                        String firstCategory = menuData.keySet().iterator().next();
                        show_menu(firstCategory);  // 여기서 자동으 midly_main도 업데트됨
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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
                // 주문 성공 시 orderList 비우기
                orderList.clear();
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
            textLabel.setFont(textLabel.getFont().deriveFont(18.0f));  // 기존 폰트의 크만 18로 키우
            
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

    

    public void show_check(){
        System.out.println("check");
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
        leftly_down.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton homeButton = new JButton("Home");
        JButton textButton = new JButton("Text");
        JButton checkButton = new JButton("Check");

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
        itemLabel.setFont(new Font(itemLabel.getFont().getName(), Font.PLAIN, 16));
        
        // 오른쪽 패널 (Qty와 Price)
        JPanel rightLabels = new JPanel();
        rightLabels.setLayout(new GridLayout(1, 2));  // 1행 2열의 GridLayout 사용
        rightLabels.setPreferredSize(new Dimension(250, rightLabels.getPreferredSize().height));  // 너비를 200에서 250으로 증가
        
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));  // 오른쪽 정렬, 간격 20
        JLabel qtyLabel = new JLabel("Qty");
        qtyLabel.setFont(new Font(qtyLabel.getFont().getName(), Font.PLAIN, 16));
        qtyPanel.add(qtyLabel);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));  // 오른쪽 정렬, 간격 20
        JLabel priceLabel = new JLabel("Price");
        priceLabel.setFont(new Font(priceLabel.getFont().getName(), Font.PLAIN, 16));
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
