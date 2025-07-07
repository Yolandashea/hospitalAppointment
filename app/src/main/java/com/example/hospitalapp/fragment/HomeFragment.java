package com.example.hospitalapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hospitalapp.activity.SelectDoctorActivity;
import com.example.hospitalapp.adapter.BannerAdapter;
import com.example.hospitalapp.adapter.DepartmentAdapter;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.Department;
import com.example.hospitalappointment.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private ViewPager2 viewPager;
    private LinearLayout indicatorContainer;
    private RecyclerView rvDepartments;
    private EditText etSearchDepartment;
    private DBManager dbManager;
    private DepartmentAdapter adapter;
    private List<BannerAdapter.BannerItem> bannerItems;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager != null) {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % bannerItems.size();
                viewPager.setCurrentItem(nextItem);
                handler.postDelayed(this, 3000);
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        indicatorContainer = view.findViewById(R.id.indicatorContainer);
        rvDepartments = view.findViewById(R.id.rv_departments);
        etSearchDepartment = view.findViewById(R.id.et_search_department);

        setupBanner();
        setupDepartments();
        setupSearch();

        return view;
    }

    private void setupSearch() {
        etSearchDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDepartments(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterDepartments(String keyword) {
        List<Department> filteredDepartments;
        if (keyword.isEmpty()) {
            filteredDepartments = dbManager.getAllDepartments();
        } else {
            filteredDepartments = dbManager.searchDepartments(keyword);
        }
        adapter.updateData(filteredDepartments);
    }

    private void setupBanner() {
        // 初始化轮播数据
        bannerItems = new ArrayList<>();
        bannerItems.add(new BannerAdapter.BannerItem(
                R.drawable.img01,
                "四川公布15项“互联网+”医疗服务价格 按乙类类别进行医保支付",
                "2024-03-21 10:00",
                "近日，记者从省医保局了解到，该局已印发相关通知，公布互联网复诊、远程会诊、远程起搏器监测等15项转归“互联网+”医疗服务价格项目和医保支付政策。\n" +
                        "\n" +
                        "　　近年来，根据国家医保局相关要求，四川陆续出台19项“互联网+”医疗服务价格项目及全省统一的收费标准，其中15项试行期已满。根据相关规定，省医保局履行相关程序，将试行到期的15项“互联网+”医疗服务价格项目转归为正式医疗服务价格项目。15项“互联网+”医疗服务价格项目，包括互联网复诊1项、远程会诊9项、远程门诊4项和远程起搏器监测1项。\n" +
                        "\n" +
                        "　　省医保局相关负责人介绍，公布的15项“互联网+”医疗服务价格为全省公立医疗机构执行标准。其中，“互联网复诊”新增全省二乙以下公立医疗机构价格3元/次。\n" +
                        "\n" +
                        "　　患者接受“互联网+”医疗服务，按服务受邀方执行的项目价格付费，涉及邀请方、受邀方及技术支持方等多个主体或涉及同一主体不同部门的，各方自行协商确定分配关系。\n" +
                        "\n" +
                        "　　公立医疗机构应以患者知情同意为前提，严格执行价格公示和明码标价制度，接受社会监督。\n" +
                        "\n" +
                        "　　据了解，15项“互联网+”医疗服务价格项目已于8月26日起施行，有效期5年。同时，按乙类类别进行医保支付。"
        ));
        bannerItems.add(new BannerAdapter.BannerItem(
                R.drawable.img02,
                "南山医院“光速”升级：F5G-A如何夯实智慧医院“数字底座”？",
                "2025-05-29",
                "每位患者的病理切片数据上传要等2分钟？AI辅助诊断结果又因网络传输慢而耽误了？在深圳市南山区人民医院（以下简称：南山医院），这些困扰行业的痛点已经成为过去。\n" +
                        "\n" +
                        "\n" +
                        "在智慧医疗快速发展的进程中，疾病的精准诊断越来越“依靠数据说话”，无论是CT、超声、磁共振等影像学检查，还是数字病理切片，越高分辨率的影像意味着越大的数据量，它们对网络传输提出了挑战。\n" +
                        "\n" +
                        "在数字病理切片场景，南山医院引入了AI辅助病理诊断，旨在解决人工诊断效率低、患者就诊时间长的问题。而一次AI诊断需要上传20张影像切片，这些微米级的切片每张大小为1GB~3GB，意味着单个患者的病理切片检查数据量高达20GB以上。\n" +
                        "\n" +
                        "对于南山医院来说，数字病理室每天要处理的切片数量多达1000张以上，过去采用传统网络需要数小时才能将这些切片数据完成上传，严重制约了AI应用效果。\n" +
                        "\n" +
                        "而就在不久前，南山医院与华为合作，在数字病理等场景成功应用50G PON，打造了全球医疗行业首个基于50G PON技术的F5G-A万兆全光医院网络解决方案，秒级便可完成一个患者的全量切片数据上传，AI辅助病理诊断，加上医生审核，以前需要1个多小时，现在5分钟便能出报告，大幅度提升了诊疗效率。\n" +
                        "\n" +
                        "F5G-A万兆全光网，助力南山医院刷新了医疗行业网络技术的新高度，进而奠定了智慧医院持续升级的坚实基础。\n" +
                        "\n" +
                        "01 从F5G到F5G-A，南山医院打造“全光医院”样板\n" +
                        "\n" +
                        "日前，华为举办园区网络“以光惠算”先锋行动发布会，作为50G PON万兆光网示范点，发布会以实地探访的形式走进南山医院，揭开了其智慧医院建设和F5G-A落地应用的面纱。\n" +
                        "\n" +
                        "深圳市南山区人民医院副院长刘岩松表示：华为F5G-A万兆全光园区方案在南山医院AI医疗落地中扮演了关键性的基础设施支撑角色，通过“网络+算力+安全”的一体化能力，不仅承载了AI医疗对海量数据传输、实时交互的刚性需求，更以绿色集约的架构为智慧医院建设提供可扩展的“数字底盘”。\n" +
                        "\n" +
                        "事实上，在部署F5G-A万兆光网前，南山医院早已展开对F5G的积极探索和实践。\n" +
                        "\n" +
                        "过去，南山医院网络采用铜缆架构，存在带宽瓶颈、管理复杂、能耗高等痛点，尤其面对HIS、EMR、PACS等医疗信息系统的不断建设，各系统对带宽、时延都有了更高的要求。\n" +
                        "\n" +
                        "F5G技术具备简架构、大带宽、低时延、高安全、易运维等优势特性，能够为医疗业务提供高速、安全、可靠的网络带宽，为运维人员提供便捷智能的运维管理手段，并且带宽升级不受限、极易演进。\n" +
                        "\n" +
                        "南山医院正是在这种背景下率先“追光”。早在2019年，其在门诊综合楼便开始试点部署F5G光网络，并在后续的二期改扩建中全面铺开，实现从单楼试点到多楼覆盖、从单一系统到办公网/业务网/设备网系统的深度承载。\n" +
                        "\n" +
                        "在网络架构上，南山医院F5G全光网架构整体采用核心-边缘设计，通过在各楼栋部署2台主备OLT，OLT上行与院区核心交换机采用2*100G互联，为整个园区网提供统一交换功能；OLT下行通过GPON/10G GPON下行覆盖病房区、办公区、医护区、公共区等信息点位，提供设备网络接入。\n" +
                        "\n" +
                        "F5G通过简化架构，组网和布线更简单，一个OLT就可以把一栋楼所有的网络管理起来，拓展性更好，故障影响范围更小，并可以根据不同区域或科室对带宽需求不同，灵活配置不同的PON技术。同时，在弱电井，用无源分光器替代了过去的有源设备，减少占用空间的同时，实现功耗的显著降低。\n" +
                        "\n" +
                        "在业务承载上，南山医院通过一张F5G全光网建设实现医院业务数据、电话语音、IPTV等融合业务承载，覆盖不同应用场景，高效承载了智慧医院的建设。\n" +
                        "\n" +
                        "02 F5G-A赋能数智医疗应用，南山医院提升智慧医院建设水平\n" +
                        "\n" +
                        "从F5G到F5G-A，南山医院持续“追光”。随着医院改扩建项目的持续进行，网络规模不断扩大，且面对AI病理诊断、医技影像、一体化手术室等医疗业务对网络带宽、时延、可靠性等提出新的要求，南山医院推动全光网络持续演进，是推动医疗智能化建设的关键支撑。\n" +
                        "\n" +
                        "在数字病理场景，南山医院通过引入F5G-A全光网，实现“50G到房间”，将单次AI辅助数字化病理诊断时间由2分钟缩短到10秒，在显著提升诊疗效率的同时，实现了AI大模型自动标记肿瘤细胞且精准度可高达初筛“零漏筛”。\n" +
                        "\n" +
                        "走进智慧病房，患者床头配备智能屏，接入医院F5G-A网络后可实时呈现诊疗信息、提供生活服务等功能。智慧病房内，每个病房均配备生命体征一体机，护士通过该设备采集的患者生命体征数据，可基于F5G-A自动上传至护理病历，并由AI系统进行智能分析，从而辅助生成个性化的健康宣教方案。这不仅显著提升了护士的工作效率，也确保患者对自身诊疗方案有更充分的了解。\n" +
                        "\n" +
                        "在智慧手术室，结合数据孪生模型，南山医院建成全国“智慧手术室”标杆，实现智能排台、智慧仓储、环境安全管控等功能。AI刷手池规范外科手消毒，智能导航提升手术精准度。这背后，大带宽、低时延、超可靠的F5G-A全光网确保诊疗信息、手术机器人、全息手术录播等医疗系统、医疗设备的稳定高效网络接入，为患者手术的质量安全提供保障。\n" +
                        "\n" +
                        "此外，基于F5G-A全光网的万物感知、门诊阅片的秒级调阅、智能化的气动物流与中型箱式物流体系、智慧安防与后勤的精细化管理等，F5G-A在智慧医院建设中展现出更广泛、更深度的应用，助推南山医院不断提升智慧化建设水平。\n" +
                        "\n" +
                        "03 以光惠算，F5G-A助力行业智能化提速\n" +
                        "\n" +
                        "看得出，在千行万业加速智能化的今天，一个更优网络底座的重要性愈加凸显，从“有网可用”迈向“以网促智”，F5G-A万兆全光园区方案正成为行业智能化升级的关键引擎。\n" +
                        "\n" +
                        "这正是华为开展园区网络“以光惠算”先锋行动的初衷。\n" +
                        "\n" +
                        "一方面，华为推动F5G-A在带宽、场景、运营、规划上的全面升级。带宽升级：50G到房间，10G到AP，满足高密Wi-Fi 7接入；场景升级：光AP从室内走向室外，让室外也可以实现光Wi-Fi全覆盖；运营升级：网管由单用户升级支持多租户、多园区共建共享，OPEX降低30%以上；规划升级：采用全新的FTTO规划工具，AI识别CAD图纸，自动输出项目预算表、配置清单等，将规划时间由两天缩短至1小时。\n" +
                        "\n" +
                        "与此同时，华为面向园区旧改、高安全园区和商业市场进行三大行业场景增强，并发布6大场景化新品，推动F5G-A万兆全光园区成为AI时代园区新标配。\n" +
                        "\n" +
                        "另一方面，为了更好地助力伙伴抓住F5G-A万兆全光园区所带来的新机遇，华为从研营销供服全方位做好支持，华为聚焦打造方案硬核竞争力，全开放营销资源，全链路资源保障伙伴销供服，从商机挖掘、项目拓展、方案开发、敏捷供应到快速交付，华为投入360人专职拓展团队、300万联合营销基金、37个样板点研学游等举措，赋能1200家伙伴，培养5000名光产业工程师，共享20亿新商机，助力伙伴赢取产业新空间，共建全光产业。\n" +
                        "\n" +
                        "随着ETSI正式发布F5G-A标准，叠加国家工信部万兆光网试点政策东风，园区网络正式迈入万兆新时代。\n" +
                        "\n" +
                        "在数智化浪潮如火如荼的发展背景下，“无光，不AI”，F5G-A不仅是网络升级，更是面向AI时代的底层架构重构。华为在教育、制造、医疗等行业推动园区网络“以光惠算”先锋行动，加速各行业“AI+”场景落地，正在为行业智能化注入“全光力量”。"
        ));
        bannerItems.add(new BannerAdapter.BannerItem(
                R.drawable.img03,
                "引领AI时代园区数智新变革 园区网络“以光惠算”先锋行动即将开启",
                "2025-05-15",
                "在数智化浪潮席卷全球的当下，园区作为产业集聚与创新发展的核心载体，其网络基础设施建设直接决定数智化转型成效。随着人工智能、物联网、大数据等新兴技术迅猛发展，园区应用场景呈现出爆发式增长与复杂化趋势。从智能工厂中的机器人实时协作、AGV小车精准运行，到智慧校园里的高清视频教学、远程互动课堂，再到智能医疗场景下的AI辅助病理诊断、远程医疗会诊等。各类应用对网络带宽、低时延、高可靠性的需求呈指数级增长。\n" +
                        "\n" +
                        "图片 1.png\n" +
                        "\n" +
                        "传统园区网络架构多依赖铜线，面对如今海量设备接入、大数据量实时传输以及复杂多样的应用场景需求时，暴露出了诸多短板。在带宽方面，传统网络难以满足高带宽业务的需求，如高清视频、VR/AR等应用常常出现卡顿现象。时延方面，无法将关键业务的时延稳定控制在较低水平，对于实时交互类应用的支持十分有限。连接数量上，随着物联网设备的激增，传统网络的接入能力逐渐饱和，导致设备掉线、网络拥塞等问题频繁出现。安全性方面，传统网络的防护体系相对薄弱，容易受到网络攻击，威胁园区内的数据安全和业务正常运行。\n" +
                        "\n" +
                        "在这样的背景下，华为在2025年华为中国合作伙伴大会期间，正式发布了F5G-A万兆全光园区解决方案。该方案通过业界最高密50G PON OLT、业界首款室外Wi-Fi 7光AP等系列新品，实现50Gbps到房间，10Gbps到AP，并采用单纤128分光技术实现联接数翻倍，让每个园区的每个人可以随时随地使用AI，实现“以光惠算”，正逐渐成为AI时代园区网络的全新标配。\n" +
                        "\n" +
                        "F5G-A是由欧洲电信标准化协会（ETSI）发布的固定网络技术标准，F5G-A万兆全光园区网络以光纤作为信息传输的主要介质，具备诸多显著优势。超大带宽能力使其能够轻松实现 50G到房间、万兆到Wi-Fi的高速网络覆盖，为各类高带宽需求应用提供充足的网络资源。无论是高清视频会议、大数据传输，还是VR/AR沉浸式体验，都能流畅运行。其低时延特性可保障数据传输的即时性，满足如工业自动化控制、实时视频交互等对时延敏感的业务场景。在工业生产中，设备之间的指令传输能够瞬间响应，确保生产流程的精准高效；在远程手术场景下，医生的操作指令可以快速准确地传达给手术机器人，保障手术的顺利进行。广连接能力则可支持海量设备的接入，契合物联网时代园区内各类物联设备激增的趋势。全光网络天然的物理隔离特性，为网络安全提供了坚实保障，再结合先进的加密技术，有效抵御各类网络攻击，保护园区内的数据安全。此外，其架构采用无源分光器替换传统有源汇聚设备，可大幅减少弱电机房、空调、消防等配套设施建设，绿色节能，减少碳足迹。\n" +
                        "\n" +
                        "从政策层面看，国家对数字经济发展的支持力度不断加大，一系列利好政策为园区网络发展提供了坚实保障。今年1月，工业和信息化部发布了《关于开展万兆光网试点工作的通知》，提出到2025年底，在有条件、有基础的城市和地区，聚焦小区、工厂园区等重点场景，开展万兆光网试点，实现50G-PON超宽光接入等技术的部署应用。随后在国新办举行的2025年一季度工业和信息化发展情况发布会上明确，在千兆光网“县县通”的基础上启动开展万兆光网试点，在全国86个城市的168个小区、工厂和园区试点部署。各地政府积极响应，纷纷制定并实施相关配套政策，推动园区网络的智能化升级。北京、上海、深圳等一线城市率先行动，将建设万兆光网示范园区纳入地方发展规划。\n" +
                        "\n" +
                        "在此机遇下，园区网络“以光惠算”先锋行动的发布会将于5月16日举行。本次发布会以“F5G-A万兆全光园区，AI时代园区标配”为主题，汇聚行业精英、专家学者以及知名企业代表，共同探讨AI时代园区网络的发展新方向、新机遇。\n" +
                        "\n" +
                        "活动开场，华为政企光领域总裁杨曦将带来“F5G-A万兆全光园区，AI时代园区标配”主题演讲，深入剖析F5G-A万兆全光园区网络在AI时代的重要意义和前景。\n" +
                        "\n" +
                        "来自南山医院、湖北大学、徐工动力等单位的代表，将结合自身实际，分享F5G-A万兆全光园区网络在医疗、教育、制造等领域的应用成果和经验，这些案例将全方位展示F5G-A 万兆全光园区网络在不同行业、不同场景下的价值，为园区网络建设提供借鉴和参考。\n" +
                        "\n" +
                        "此外，华为中国政企商业MKT与解决方案销售部部长浦强将携手伙伴，共同发布园区网络“以光惠算”先锋行动，旨在凝聚产业各方力量，整合行业优质资源，共同推动园区网络的数智化升级进程。\n" +
                        "\n" +
                        "工业和信息化部提出，力争到2027年，工业大省大市和重点园区规上工业企业实现数字化改造全覆盖，建成200个左右高标准数字园区；到2030年力争规上工业企业基本完成一轮数字化改造。在AI蓬勃发展的今天，园区网络“以光惠算”先锋行动发布会的举办恰逢其时。它不仅是一场汇聚行业智慧、展示创新成果的盛会，更是推动园区数智化发展的重要引擎。此次发布会将为园区网络发展注入全新活力与动力，引领园区网络朝着更加智能、高效、绿色、安全的方向大步迈进。让我们共同期待5月16日发布会的精彩呈现，携手共赢AI时代园区数智新未来。"
        ));
        // 可以添加更多轮播项...

        // 设置适配器
        BannerAdapter adapter = new BannerAdapter(requireContext(), bannerItems);
        viewPager.setAdapter(adapter);

        // 设置指示器
        setupIndicators();

        // 设置页面切换监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
            }
        });

        // 开始自动轮播
        startAutoScroll();
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[bannerItems.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(requireContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    i == 0 ? R.drawable.indicator_active : R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(params);
            indicatorContainer.addView(indicators[i]);
        }
    }

    private void updateIndicators(int position) {
        int childCount = indicatorContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicatorContainer.getChildAt(i);
            imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    i == position ? R.drawable.indicator_active : R.drawable.indicator_inactive));
        }
    }

    private void setupDepartments() {
        // 设置科室列表
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvDepartments.setLayoutManager(layoutManager);
        
        dbManager = new DBManager(requireContext());
        List<Department> departments = dbManager.getAllDepartments();
        
        adapter = new DepartmentAdapter(requireContext(), departments);
        
        // 设置点击事件监听器
        adapter.setOnItemClickListener(department -> {
            Log.d(TAG, "Department clicked: " + department.getName() + ", ID: " + department.getId());
            Toast.makeText(getContext(), "正在跳转到" + department.getName(), Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(getContext(), SelectDoctorActivity.class);
            intent.putExtra("department", department.getId());
            startActivity(intent);
        });
        
        rvDepartments.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void startAutoScroll() {
        handler.postDelayed(runnable, 3000);
    }
}
