import { Button, Modal, Tabs, message } from "antd";
import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import * as request from "~/utils/httpRequest";
import OrderItem from "./OrderItem";
import Loading from "~/components/Loading/Loading";
import { toast } from "react-toastify";

function NewOrder() {
  const [listOrder, setListOrder] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const timeout = setTimeout(() => {
      loadOrders();
      setLoading(false);
    }, 1000);
    return () => clearTimeout(timeout);
  }, []);

  const loadOrders = () => {
    request
      .get(`bill`, {
        params: {
          idStaff: 1,
          status: 1
        }
      }).then((response) => {
        setListOrder(response.data);
        console.log(response.data);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleCreate = () => {
    request
      .post("/bill", {})
      .then((response) => {
        if (response.status === 200) {
          toast.success("Tạo mới thành công");
          loadOrders();
        }
      })
      .catch((e) => {
        toast.error(e.response.data);
      });
  };

  const handleDelete = (key) => {
    const idBill = listOrder[key].id;
    Modal.confirm({
      title: "Xác nhận",
      maskClosable: true,
      content: `Xác nhận xóa đơn hàng ${listOrder[key].code}?`,
      okText: "Ok",
      cancelText: "Cancel",
      onOk: async () => {
        request.remove(`/bill/${idBill}`).then(response => {
          console.log(response);
          message.success("Xóa thành công!");
          loadOrders();
        }).catch(e => {
          console.log(e);
        })
      },
    });
  }

  if (loading) {
    return (
      <>
        <Loading></Loading>
      </>
    )
  }

  return (
    <>
      <div className="d-flex">
        <div className="flex-grow-1">
          <Button onClick={() => handleCreate()} className="bg-warning text-dark" type="primary">Tạo mới đơn hàng</Button>
        </div>
        <div className="">

        </div>
      </div>
      <div className="mt-3">
        <Tabs hideAdd type="editable-card" onEdit={handleDelete}>
          {listOrder.length > 0 && listOrder.map((order, index) => (
            <Tabs.TabPane key={order.code} tab={`${order.code}`}>
              <OrderItem props={order} index={index + 1} onSuccess={loadOrders} />
            </Tabs.TabPane>
          ))}
        </Tabs>
      </div>
    </>
  );
}

export default NewOrder;
