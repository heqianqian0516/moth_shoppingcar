package com.bwei.moth_shoppingcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapter.LeftAdapter;
import adapter.RightAdapter;
import bean.Goods;
import bean.Result;
import bean.Shop;
import core.DataCall;
import presenter.CartPresenter;

public class MainActivity extends AppCompatActivity implements DataCall<List<Shop>> {
    private TextView mSumPrice;
    private TextView mCount;
    private RecyclerView mLeftRecycler,mRightRecycler;

    private LeftAdapter mLeftAdapter;
    private RightAdapter mRightAdapter;

    private CartPresenter cartPresenter = new CartPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSumPrice = findViewById(R.id.goods_sum_price);
        mCount = findViewById(R.id.goods_number);
        mLeftRecycler = findViewById(R.id.left_recycler);
        mRightRecycler = findViewById(R.id.right_recycler);

        mLeftRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRightRecycler.setLayoutManager(new LinearLayoutManager(this));

        mLeftAdapter = new LeftAdapter();
        mLeftAdapter.setOnItemClickListenter(new LeftAdapter.OnItemClickListenter() {
            @Override
            public void onItemClick(Shop shop) {
                mRightAdapter.clearList();//清空数据
                mRightAdapter.addAll(shop.getList());
                mRightAdapter.notifyDataSetChanged();
            }
        });
        mLeftRecycler.setAdapter(mLeftAdapter);
        mRightAdapter = new RightAdapter();
        mRightAdapter.setOnNumListener(new RightAdapter.OnNumListener() {
            @Override
            public void onNum() {
                calculatePrice(mLeftAdapter.getList());
            }
        });
        mRightRecycler.setAdapter(mRightAdapter);

        cartPresenter.requestData();
    }

    @Override
    public void success(List<Shop> data) {

        calculatePrice(data);//计算价格和数量

        mLeftAdapter.addAll(data);//左边的添加类型

        //得到默认选中的shop，设置上颜色和背景
        Shop shop = data.get(1);
        shop.setTextColor(0xff000000);
        shop.setBackground(R.color.white);
        mRightAdapter.addAll(shop.getList());



        mLeftAdapter.notifyDataSetChanged();
        mRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(this, result.getCode() + "   " + result.getMsg(), Toast.LENGTH_LONG).show();
    }

    /**
     * @author dingtao
     * @date 2018/12/18 7:01 PM
     * 计算总价格
     */
    private void calculatePrice(List<Shop> shopList){
        double totalPrice=0;
        int totalNum = 0;
        for (int i = 0; i < shopList.size(); i++) {//循环的商家
            Shop shop = shopList.get(i);
            for (int j = 0; j < shop.getList().size(); j++) {
                Goods goods = shop.getList().get(j);
                //计算价格
                totalPrice = totalPrice + goods.getNum() * goods.getPrice();
                totalNum+=goods.getNum();//计数
            }
        }
        mSumPrice.setText("价格："+totalPrice);
        mCount.setText(""+totalNum);
    }
}
