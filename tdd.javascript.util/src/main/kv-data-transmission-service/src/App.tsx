import React from 'react';
import './App.css';
import {Button, Col, Input, Row, Space, Typography} from 'antd';
import {get} from 'http';

const {TextArea} = Input;
const {Title} = Typography;


interface State {
    operator: string;
    partition: string;
    schema: string;
    odpsSpace: string;
    tableStoreSpace: string;
    tableStoreName: string;
    primaryKeyCount: number;
    jsonConfig: string;
}

interface TableInfo {
    tableName: string;
    columns: {
        name: string,
        type: string
    }[];
}

export function extractTableInfo(ddl: string) {
    const tableRegex = /create\s+table\s+(\w+)\s+/i;
    const columnRegex = /\(([\s\S]*?)\)/;

    const tableMatch = ddl.match(tableRegex);
    const columnMatch = ddl.match(columnRegex);

    if (!tableMatch || !columnMatch) {
        throw new Error('DDL 解析错误');
    }

    const tableName = tableMatch[1];
    const columnText = columnMatch[1];

    const columns = columnText.split(',').map((c) => {
        const [name, type] = c.trim().split(/\s+/);
        return {name, type};
    });

    return {
        tableName,
        columns,
    };
}

function generateJsonConfigObj(tableInfo: TableInfo, state:State) {
    const {tableName, columns} = tableInfo;
    console.log(JSON.stringify(tableInfo));
    const {operator, partition, odpsSpace, tableStoreSpace, tableStoreName,primaryKeyCount} = state;
    return {
        baseId: operator,
        type: "job",
        version: "2.0",
        steps: [
            {
                stepType: "odps",
                parameter: {
                    partition: partition,
                    isCompress: false,
                    indexes: [],
                    datasource: "_ODPS",
                    envType: 1,
                    isSupportThreeModel: false,
                    column: columns.map(it => it),
                    guid: `${odpsSpace}.${tableName}`,
                    table: tableName,
                }
            },
            {
                stepType: "ots",
                parameter: {
                    datasource: tableStoreSpace,
                    envType: 1,
                    primaryKey: columns.slice(0,3).map(it => it),
                    column: columns.slice(primaryKeyCount,columns.length).map(it => it),
                    table: tableStoreName,
                    writeMode: "PutRow"
                },
                name:"Writer",
                category:"writer"
            },
            {
                copies:1,
                parameter:{nodes:[],edges:[],groups:[],version:"2.0"},
                name:"Processor",
                category:"processor"
            }
        ],
        setting:{
            jvmOption:"",errorLimit:{record:"0"},locale:"zh",speed:{throttle:false,concurrent:3}
        },
        order:{
          hops:[{
              from:"Reader",to:"Writer"
          }]
        }
    }
}

const TableStoreDataTransmissionServiceConfigGenerate = (): JSX.Element => {
    const [state, setState] = React.useState<State>({
        operator: '',
        partition: "ds = $bizdate",
        schema: '',
        odpsSpace: 'odps.c2m_supply',
        tableStoreSpace: '',
        tableStoreName: '',
        primaryKeyCount: 3,
        jsonConfig: ''
    });
    const {operator, odpsSpace, partition, schema, jsonConfig, tableStoreName,tableStoreSpace,primaryKeyCount} = state;
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setState({...state, [name]: value});
    };


    const generateJsonConfig = () => {
        const tableInfo = extractTableInfo(schema.trim());
        const jsonConfigObj = generateJsonConfigObj(tableInfo,state);
        const jsonConfig = JSON.stringify(jsonConfigObj, null, 2);
        setState(
            (prevState) => {
                return ({...prevState, jsonConfig});
            }
        );
    };

    return (
        <div className="container">
            <Title level={3}>TableStore数据同步配置器</Title>
            <Space direction="vertical" className={"space"}>
                <Row>
                    <Col span={6}>
                        <Input name="operator" value={operator} onChange={handleChange} placeholder="操作者工号"/>
                    </Col>
                    <Col span={12}>
                        <Input name="odpsSpace" value={odpsSpace} onChange={handleChange}
                               placeholder={`项目空间名,默认 ${odpsSpace}`}/>
                    </Col>
                    <Col span={6}>
                        <Input name="partition" value={partition} onChange={handleChange}
                               placeholder={`分区信息,默认 ${partition}`}/>
                    </Col>
                </Row>
                <Row>
                    <Col span={6}>
                        <Input name="tableStoreSpace" value={tableStoreSpace} onChange={handleChange}
                               placeholder={`TableSore 实例名`}/>
                    </Col>
                    <Col span={12}>
                        <Input name="tableStoreName" value={tableStoreName} onChange={handleChange}
                               placeholder={`TableSore 表名`}/>
                    </Col>
                    <Col span={6}>
                        <Input name="primaryKeyCount" value={primaryKeyCount} onChange={handleChange}
                               placeholder={`主键个数,最大4`}/>
                    </Col>
                </Row>
                <Row>
                    <Col span={12}>
                        <Input name="schema" value={schema} onChange={handleChange} placeholder="DDL语句"/>
                    </Col>
                </Row>
                <Row>
                    <Col span={12}>
                        <Button type={"primary"} onClick={generateJsonConfig}
                                className={"generation-button"}>生成同步配置</Button>
                    </Col>
                </Row>
                <Row>
                    <Col span={12}>
                        <TextArea value={jsonConfig} className={"text-area"}/>
                    </Col>
                </Row>
            </Space>
        </div>
    );
};

export default TableStoreDataTransmissionServiceConfigGenerate;
