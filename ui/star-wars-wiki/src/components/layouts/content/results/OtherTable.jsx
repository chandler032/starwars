import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";

const OtherTable = ({ data }) => {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Film Count</TableCell>
            <TableCell>Films</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.result.map((item, index) => (
            <TableRow key={index}>
              <TableCell>{item.name}</TableCell>
              <TableCell>{item.filmList?.length}</TableCell>
              <TableCell>{item.filmList?.map(film => film.title).join(", ")}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default OtherTable;