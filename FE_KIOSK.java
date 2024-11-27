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



public class FE_KIOSK extends JFrame {
    //전역 변수 사용
    private int table_num = 1;
    private Map<String, List<Menu>> menuData = new LinkedHashMap<>();
    private Map<String, List<Order_list>> orderList = new LinkedHashMap<>();
    private Container c;
    private Container left_c;
    private Container mid_c;
    private Container right_c;
    private JPanel leftly_main;
    private JPanel leftly;
    private JPanel midly_main;
    private JPanel midly;
    private JScrollPane midly_menu;
    private JPanel rightly_main;
    private JPanel rightly;
    private JPanel rightly_down;
    String store_name = "카페 명"; // 나중에 입력받는 형태 로 지정가능

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



        
        left_c = new JPanel(); // 각 왼 중 오 컨테이너를 패널로 바꾸면 조절 가능
        left_c.setLayout(new BoxLayout(left_c, BoxLayout.Y_AXIS));
        // 왼쪽 컨테이너 크기 고정
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

        right_c = new JPanel();
        right_c.setLayout(new BoxLayout(right_c, BoxLayout.Y_AXIS));
        // 오른쪽 컨테이너 크기 고정
        Dimension rightSize = new Dimension((int)(getWidth() * 0.25), getHeight());
        right_c.setPreferredSize(rightSize);
        right_c.setMaximumSize(rightSize);
        right_c.setMinimumSize(rightSize);

        //mid_c.setMaximumSize(new Dimension(Short.MAX_VALUE, mid_c.getPreferredSize().height));//????

        // 카테고리부분 왼쪽
        leftly_main = new JPanel();
        leftly_main.setLayout(new BoxLayout(leftly_main, BoxLayout.Y_AXIS));
        leftly = new JPanel();
        leftly.setLayout(new BoxLayout(leftly, BoxLayout.Y_AXIS)); // basic_load(leftly_main);
        basic_load(leftly_main);
        conn_be();
        
        // 메뉴 보이는 부분 중간
        midly_main = new JPanel();
        midly_main.setLayout(new BoxLayout(midly_main, BoxLayout.Y_AXIS));
        
        midly = new JPanel();
        midly.setLayout(new GridBagLayout());
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 500));

        midly_menu = new JScrollPane(midly);
        midly_menu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        

        // 계산 하는 부분 오른쪽
        rightly_main = new JPanel(); // 레이아웃 미정
        rightly = new JPanel();
        rightly.setLayout(new BoxLayout(rightly, BoxLayout.Y_AXIS));
        rightly_down = new JPanel();
        
        // 스크롤 패널 추가 및 테두리 제거
        JScrollPane rightlyScroll = new JScrollPane(rightly);
        rightlyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightlyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightlyScroll.setBorder(BorderFactory.createEmptyBorder());  // 테두리 제거
        rightlyScroll.getViewport().setBackground(rightly.getBackground());  // 배경색 통일

        // 뷰포트 테두리도 제거
        rightlyScroll.getViewport().setBorder(null);
        
        rightly_down = new JPanel();
        
        left_c.add(leftly_main);
        left_c.add(leftly);

        mid_c.add(midly_main);
        mid_c.add(midly_menu);

        right_c.add(rightlyScroll);  // rightly 대신 rightlyScroll 추가
        right_c.add(rightly_down);

        gbc.gridx = 0;
        gbc.weightx = 0.3; // 30% 가로 비율
        c.add(left_c, gbc);
        //c.add(left_c);

        gbc.gridx = 1;
        gbc.weightx = 0.4; // 40% 가로 비율
        c.add(mid_c, gbc);
        //c.add(mid_c);

        gbc.gridx = 2;
        gbc.weightx = 0.3; // 30% 가로 비율
        c.add(right_c, gbc);
        //c.add(right_c);

        // 기본 데이터 로드
        load_data();

        // 카테고리 버튼 추가
        //addcategorybt(leftly);

        setVisible(true);
    }

    private void show_menu(String category){
        // midly_main에 카테고리 정보 업데이트
        midly_main.removeAll();
        JLabel titleLabel = new JLabel(
            "<html>" + category + "<br>Choose Dishes</html>"
        );
        midly_main.add(titleLabel);
        midly_main.revalidate();
        midly_main.repaint();

        // 기존의 메뉴 표시 로직
        midly.removeAll();
        List<Menu> menuList = menuData.get(category);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 간격
        gbc.fill = GridBagConstraints.NONE; // 버튼이 자신의 크기를 유지하도록 설정
        gbc.anchor = GridBagConstraints.NORTH; // 위쪽 정렬
        gbc.weightx = 0; // 가로 가중치 제거
        gbc.weighty = 0; // 세로 가중치 제거

        // 마지막 행에 가중치를 주기 위한 더미 컴포넌트 추가 위치 계산
        int lastRowIndex = (menuList.size() - 1) / 3;

        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);

            try {
                URL url = new URL(menu.imageUrl);
                JButton menubt = new JButton(
                    "<html>" + 
                    "<div style='text-align: center;'>" +
                    "<img src='" + url + "' width='100' height='100'><br>" +
                    menu.name + "<br>" + 
                    menu.price + "원" +
                    "</div>" +
                    "</html>"
                );
                menubt.addActionListener(e -> {
                    System.out.println("클릭된 메뉴: " + menu.name);
                    addmenulist(rightly, menu.name, menu.price, menu.imageUrl, menu.id, 1);
                });
                menubt.setEnabled(menu.available);
                menubt.setPreferredSize(new Dimension(300, 300));

                gbc.gridx = i % 3; // 열 위치
                gbc.gridy = i / 3; // 행 위치

                // 마지막 행의 컴포넌트인 경우 나머지 공간을 채우도록 설정
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
                    itemPanel.setPreferredSize(new Dimension(400, 160));  // 세로 크기 160으로 감소
                    itemPanel.setMaximumSize(new Dimension(400, 160));
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
                    
                    JLabel totalLabel = new JLabel(total + "원");
                    totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 가운데 정렬
                    totalPanel.add(Box.createVerticalGlue());  // 상단 여백
                    totalPanel.add(totalLabel);
                    totalPanel.add(Box.createVerticalGlue());  // 하단 여백

                    topPanel.add(imageLabel, BorderLayout.WEST);
                    topPanel.add(infoPanel, BorderLayout.CENTER);
                    topPanel.add(totalPanel, BorderLayout.EAST);

                    // 하단 패널
                    JPanel bottomPanel = new JPanel(new BorderLayout());
                    bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));  // 하단 여백 10으로 감소
                    
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
                    itemPanel.add(bottomPanel);

                    rightly.add(itemPanel);
                    rightly.add(Box.createVerticalStrut(5));  // 메뉴 간 간격 5로 감소

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        // rightly_down 패널에 총 가격 표시
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
        JLabel totalPriceLabel = new JLabel(String.format("총 주문금액: %,d원", totalPrice));
        totalPriceLabel.setFont(new Font(totalPriceLabel.getFont().getName(), Font.BOLD, 16));
        totalPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 결제하기 버튼
        JButton orderButton = new JButton("결제하기");
        orderButton.setPreferredSize(new Dimension(150, 50));
        orderButton.setMaximumSize(new Dimension(150, 50));
        orderButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightly_down.add(Box.createVerticalStrut(10));
        rightly_down.add(totalPriceLabel);
        rightly_down.add(Box.createVerticalStrut(10));
        rightly_down.add(orderButton);
        rightly_down.add(Box.createVerticalStrut(10));

        rightly_down.revalidate();
        rightly_down.repaint();
        right_c.revalidate();
        right_c.repaint();
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

            menuData.clear(); // 기존 데이터 초기화

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

                // 카테고리별 메뉴 리스트에 추가
                menuData.putIfAbsent(category, new ArrayList<>());
                menuData.get(category).add(menu);
            }

            // 데이터 로딩이 완료된 후 EDT에서 UI 업데이트
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // 카테고리 버튼 추가
                    addcategorybt(leftly);
                    
                    //  번째 카테고의 메뉴 표시
                    if (!menuData.isEmpty()) {
                        String firstCategory = menuData.keySet().iterator().next();
                        show_menu(firstCategory);  // 여기서 자동으 midly_main도 업데이트됨
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }     
    }



    

    //카테고리 leftly에 동적 추가하는 부분 (처음에 leftly 부분 초기화후 새로고침시 다시 작동하게 하면 동적 수정 가능)
    private void addcategorybt(JPanel leftly){
        leftly.removeAll();
        for (String category : menuData.keySet()) {
            JButton categoryButton = new JButton(category);
            categoryButton.setPreferredSize(new Dimension(200, 80));
            categoryButton.setFont(new Font(categoryButton.getFont().getName(), Font.BOLD, 18));
            categoryButton.addActionListener(e -> {
                System.out.println("카테고리 버튼 클릭: " + category);
                // 카테고리 버튼 클릭시 로직
                show_menu(category);
            });
            leftly.add(categoryButton);
        }
    
        left_c.revalidate();
        left_c.repaint();
    }
    // 여기 html 태그 사용가능 css도 가능하지만 부분적으로 불가능할 수 있다고 함 ..(확인필요)
    // 가운데 카테고리 버튼 클릭시 그 카테고리 정보 출력 JLabel로 나중에 사진 추가 가능


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

    private void rotate_data(){ // time 설정 해야함 모르겠음 ;;
        
    }

    private void basic_load(JPanel leftly_main){
        // 상점 이름 라벨 생성
        JLabel storeLabel = new JLabel(store_name);
        
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MM - dd");
        String formattedDate = dateFormat.format(currentDate);
        JLabel dateLabel = new JLabel(formattedDate);

        // leftly_main 패널에 상점명과 날짜 라벨 추가
        leftly_main.add(storeLabel);
        leftly_main.add(dateLabel);

        // 레이아웃 갱신
        leftly_main.revalidate();
        leftly_main.repaint();
    }

    public static void main(String[] args){
        FE_KIOSK kiosk = new FE_KIOSK();
        //kiosk.conn_be();  
    }   
}
