import React from 'react';
import {render, screen} from '@testing-library/react';
import TableStoreDataTransmissionServiceConfigGenerate, {extractTableInfo} from './App';

test('renders learn react link', () => {
    render(<TableStoreDataTransmissionServiceConfigGenerate/>);
    const linkElement = screen.getByText(/learn react/i);
    expect(linkElement).toBeInTheDocument();
});

const validDDL = `
    CREATE TABLE supplier_bill
    (
        id              INT PRIMARY KEY,
        supplier_name   STRING,
        bill_no         STRING,
        amount          DECIMAL,
        settlement_date STRING,
        bill_type       STRING,
        supplier_id     INT,
        entry_time      TIMESTAMP
    )`;

const invalidDDL = 'CREATE INDEX ON mytable (name)';

describe('extractTableAndColumns', () => {
    test('should extract table name and columns from valid DDL statement', () => {
        const result = extractTableInfo(validDDL);
        expect(result).toEqual({
            tableName: 'supplier_bill',
            columns: [
                {
                    "name": "id",
                    "type": "INT"
                },
                {
                    "name": "supplier_name",
                    "type": "STRING"
                },
                {
                    "name": "bill_no",
                    "type": "STRING"
                },
                {
                    "name": "amount",
                    "type": "DECIMAL"
                },
                {
                    "name": "settlement_date",
                    "type": "STRING"
                },
                {
                    "name": "bill_type",
                    "type": "STRING"
                },
                {
                    "name": "supplier_id",
                    "type": "INT"
                },
                {
                    "name": "entry_time",
                    "type": "TIMESTAMP"
                }
            ]
        });
    });

    test('should throw error for invalid DDL statement', () => {
        expect(() => extractTableInfo(invalidDDL)).toThrowError("DDL 解析错误");
    });
});
